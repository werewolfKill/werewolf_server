package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.config.Config;
import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.entity.*;
import com.zinglabs.zwerewolf.entity.role.Guard;
import com.zinglabs.zwerewolf.entity.role.Prophet;
import com.zinglabs.zwerewolf.entity.role.UserRole;
import com.zinglabs.zwerewolf.entity.role.Witch;
import com.zinglabs.zwerewolf.manager.IMBusinessManager;
import com.zinglabs.zwerewolf.manager.IMMessageManager;
import com.zinglabs.zwerewolf.service.GameService;
import com.zinglabs.zwerewolf.service.UserService;
import com.zinglabs.zwerewolf.util.ByteBufUtil;
import com.zinglabs.zwerewolf.util.GameUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * 游戏流程控制器
 *
 * @author wangtonghe
 * @date 2017/7/24 08:49
 */
public class GameController implements BaseController {

    private GameService gameService = new GameService();

    private UserService userService = new UserService();

    @Override
    public void doDestory(Map application) {

    }

    @Override
    public void doAccept(short commandId, Channel channel, ByteBuf body, Map<String, Object> application) {
        RequestBody requestBody = ByteBufUtil.resolveGame(body);
        int fromId = requestBody.getFromId();
        int roomId = requestBody.getRoomId();
        int code = requestBody.getCode();
        int bout = requestBody.getBout();
        Map<UserChannel, ResponseBody> msgGourp = new HashMap<>();
        Map<Integer, UserChannel> roomChannels = userService.getByRoomId(roomId);
        Timer timer = new Timer();

        Room room = null;
        GameInfo gameInfo;
        ResponseBody responseBody;
        UserChannel userChannel;

        switch (commandId) {
            case ProtocolConstant.CID_GAME_READY_REQ:  //准备、离开游戏
                int ready = gameService.readyGame(fromId, roomId);
                ResponseBody readyBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_READY_RESP,
                        fromId, ready);
                roomChannels.forEach((id, chan) -> msgGourp.put(chan, readyBody));
                IMBusinessManager.sendGroup(msgGourp);
                break;
            case ProtocolConstant.CID_GAME_START_REQ: //开始游戏
                room = gameService.checkAndGetRoom(roomId, fromId);
                if (room == null) {    //房间为空
                    responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_START_FAIL,
                            fromId, Config.ROOM_NOT_EXIST);
                    userChannel = roomChannels.get(fromId);
                    msgGourp.put(userChannel, responseBody);
                } else if (room.getCurNumber() < room.getNumber()) {   //人数不够
                    responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_START_FAIL,
                            fromId, Config.ROOM_NOT_ENOUGH_NUM);
                    userChannel = roomChannels.get(fromId);
                    msgGourp.put(userChannel, responseBody);
                } else {                      //开始游戏
                    Map<Integer, UserRole> roleMap = gameService.allotRoles(room);
                    room.getGameInfoMap().put(0, new NightInfo());  //设置每夜死亡信息
                    List<Integer> wolfs = new ArrayList<>();
                    roleMap.forEach((id, ur) -> {
                        if (ur.getRoleId() == Config.ROLE_CODE_OF_WOLF) {
                            wolfs.add(ur.getUserId());
                        }
                    });
                    roleMap.forEach((id, ur) -> {
                        UserChannel uchan = roomChannels.get(id);
                        ResponseBody resBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_START_RESP,
                                fromId, ur.getRoleId());
                        Map<String, Object> param = new HashMap<>();
                        param.put("wolfs", wolfs);
                        if (ur.getRoleId() == Config.ROLE_CODE_OF_WOLF) {
                            resBody.setParam(param);
                        }
                        msgGourp.put(uchan, resBody);
                    });
                }
                IMBusinessManager.sendStartMsg(msgGourp);
                doDawnTimer(roomId, bout, 30 * 1000);
                break;
            case ProtocolConstant.CID_GAME_KILL_REQ:   //狼人杀人
                room = gameService.checkAndGetRoom(roomId);
                if (room == null) {
                    return;
                }
                gameInfo = gameService.getGameInfo(roomId);
                if (gameInfo == null) {
                    gameInfo = new GameInfo();
                }
                responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_KILL_RESP,
                        fromId, code);  //code表示要杀的人的id
                gameInfo.putKillInfo(code);
                Map<Integer, UserChannel> wolfChannel = getWolfs(roomChannels, room.getPlayers());
                wolfChannel.forEach((id, chan) -> msgGourp.put(chan, responseBody));
                IMBusinessManager.sendGroup(msgGourp);  //告知同伴自己击杀的人
                int wolfSize = wolfChannel.size();

                if (wolfSize == gameInfo.getKillNumber() || gameInfo.getKillNumber() == 1 /* 测试*/) {
                    int killed = gameInfo.getKilled();  //得到被杀的人
                    room.getGameInfoMap().get(bout).setKillId(code);
                    room.addKilled(killed);

                    Witch witch = (Witch) GameUtil.getRoleFromRoleMap(room.getPlayers(), Config.ROLE_CODE_OF_WITCH);
                    if (witch!=null&&witch.getSaveId() > 0) {  //已用过解药
                        break;
                    }
                    //向女巫告知杀人信息
                    ResponseBody killBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_NOTIFY_WITCH_KILLED,
                            0, killed);
                    Map<Integer, UserChannel> witchChannel = getRoleMapById(roomChannels, room.getPlayers(), Config.ROLE_CODE_OF_WITCH);
                    msgGourp.clear();
                    witchChannel.forEach((id, chan) -> msgGourp.put(chan, killBody));
                    IMBusinessManager.sendGroup(msgGourp);
                }
                break;
            case ProtocolConstant.CID_GAME_SAVE_REQ:  //女巫救人请求
                room = gameService.checkAndGetRoom(roomId);
                if (room == null) {
                    return;
                }
                room.getGameInfoMap().get(bout).setSaveId(code);
                Witch saveWitch = (Witch) room.getPlayers().get(fromId).getRole();
                saveWitch.setSaveId(code);
                break;
            case ProtocolConstant.CID_GAME_POISON_REQ:  //女巫毒人请求
                room = gameService.checkAndGetRoom(roomId);
                if (room == null) {
                    return;
                }
                room.getGameInfoMap().get(bout).setPoisonId(code);
                Witch pwitch = (Witch) room.getPlayers().get(fromId).getRole();
                pwitch.setPoisonId(code);
                break;
            case ProtocolConstant.CID_GAME_VERIFY_REQ:  //预言家验人
                room = gameService.checkAndGetRoom(roomId);
                if (room == null) {
                    return;
                }
                int roleId = room.getPlayers().get(fromId).getRoleId();
                boolean isGood = GameUtil.verify(roleId);
                Prophet prophet = (Prophet) room.getPlayers().get(fromId).getRole();
                prophet.setVerifyMap(code, isGood);
                int isGoodReply = isGood ? 1 : 0;
                responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_VERIFY_RESP,
                        code, isGoodReply);
                userChannel = roomChannels.get(fromId);
                msgGourp.put(userChannel, responseBody);
                IMBusinessManager.sendGroup(msgGourp);
                break;
            case ProtocolConstant.CID_GAME_GUARD_REQ:   //守卫守人
                room = gameService.checkAndGetRoom(roomId);
                if (room == null) {
                    return;
                }
                Guard guard = (Guard) room.getPlayers().get(fromId).getRole();
                guard.setGuardian(code);
                room.getGameInfoMap().get(bout).setGuardianId(code);
                break;
            case ProtocolConstant.CID_GAME_ASK_CHIEF: //请求竞选警长
                room = gameService.checkAndGetRoom(roomId);
                if (room == null) {
                    return;
                }
                if (code == 0) {   //表示不竞选警长
                    break;
                }
                GameInfo chiefInfo = gameService.getGameInfo(roomId);
                chiefInfo.addChiefVotes(fromId);
                responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_ASK_CHIEF_RESP,
                        fromId, code);
                roomChannels.forEach((id, chan) -> msgGourp.put(chan, responseBody));
                IMBusinessManager.sendGroup(msgGourp);
                timer.schedule(new TimerTask() {  //20秒钟时间考虑竞选或退选
                    @Override
                    public void run() {
                        int rid = chiefInfo.getChiefVotes().get(0); //随机选取一人
                        ResponseBody voteBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_POLICE_START_SPEAKING,
                                fromId, rid);
                        roomChannels.forEach((id, chan) -> msgGourp.put(chan, voteBody));
                        IMBusinessManager.sendGroup(msgGourp);
                    }
                },1000*20);
                break;
            case ProtocolConstant.CID_GAME_QUIT_POLICE: //取消竞选警长
                room = gameService.checkAndGetRoom(roomId);
                if (room == null) {
                    return;
                }
                gameInfo = gameService.getGameInfo(roomId);
                if (gameInfo == null) {
                    gameInfo = new GameInfo();
                }
                gameInfo.addQuitChiefs(fromId);
                responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_QUIT_POLICE_RESP,
                        fromId, fromId);
                roomChannels.forEach((id, chan) -> msgGourp.put(chan, responseBody));
                IMBusinessManager.sendGroup(msgGourp);
                break;
            case ProtocolConstant.CID_GAME_POLICE_SPEAKING_END:  //警上发言结束
                room = gameService.checkAndGetRoom(roomId);
                if (room == null) {
                    return;
                }
                gameInfo = gameService.getGameInfo(roomId);
                responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_START_CHIEF_VOTE,
                        fromId, fromId);
                List<Integer> ChiefList = gameInfo.getChiefVotes();
                gameInfo.addSpeakEndNum();
                if(gameInfo.getSpeakEndNum()==gameInfo.getVotePoliceNum()){
                    roomChannels.forEach((id, chan) -> {
                        if (!ChiefList.contains(id)) {  //向未上警玩家发请求
                            msgGourp.put(chan, responseBody);
                        }
                    });
                    IMBusinessManager.sendGroup(msgGourp);
                }
                break;
            case ProtocolConstant.CID_GAME_CHIEF_VOTE:  //警上投票
                room = gameService.checkAndGetRoom(roomId);
                if (room == null) {
                    return;
                }
                gameInfo = gameService.getGameInfo(roomId);
                gameInfo.putVoteInfo(code);
                doVoteTimer(gameInfo,1000*10,Config.VOTE_TYPE_CHIEF,roomChannels,room);

                break;
            case ProtocolConstant.CID_GAME_CHIEF_DECIDE_SPEAK: //开始发言
                room = gameService.checkAndGetRoom(roomId);
                int speakBase,speakId;
                if (room == null) {
                    return;
                }
                //上一夜死亡名单
                List<Integer> list = room.getGameInfoMap().get(bout-1).getDeadList();
                if(list.size()>0){
                    speakBase = list.get(0);
                }else{
                    speakBase = room.getChief();
                }
                int size = room.getLiveList().size();
                speakId = room.getLiveList().get(0);  //目前第一个发言
                responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_START_SPEAKING,
                        0, speakId);
                roomChannels.forEach((id, chan) -> msgGourp.put(chan, responseBody));
                IMBusinessManager.sendGroup(msgGourp);
                break;
            case ProtocolConstant.CID_GAME_REQ_VOTE:  //请求投票 TODO 类似请求投票，统计请求次数问题统一处理
                room = gameService.checkAndGetRoom(roomId);
                if (room == null) {
                    return;
                }
                Integer chief = room.getChief();
                gameInfo = gameService.getGameInfo(roomId);
                gameInfo.addAskVote(bout);
                if(gameInfo.getAskVote(bout)<room.getLiveList().size()||gameInfo.getAskVote(bout)==2){
                    return;
                }
                if(chief>0){
                    responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_CHIEF_REQ_SUM_TICKET,
                            0, 0);
                    int chiefId = GameUtil.getIdByPos(room.getPlayers(),chief);
                    userChannel = roomChannels.get(chiefId);
                    msgGourp.put(userChannel, responseBody);
                    IMBusinessManager.sendGroup(msgGourp);
                }else{
                    responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_REQ_VOTE_RESP,
                        0, 0);
                    roomChannels.forEach((id, chan) -> msgGourp.put(chan, responseBody));
                    IMBusinessManager.sendGroup(msgGourp);
                }
                break;
            case ProtocolConstant.CID_GAME_CHIEF_SUM_TICKET://警长归票
                room = gameService.checkAndGetRoom(roomId);
                if (room == null) {
                    return;
                }
                responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_REQ_VOTE_RESP,
                        0, code);
                roomChannels.forEach((id, chan) -> msgGourp.put(chan, responseBody));
                IMBusinessManager.sendGroup(msgGourp);

                gameInfo = gameService.getGameInfo(roomId);
                doVoteTimer(gameInfo,1000*10,Config.VOTE_TYPE_COMMON,roomChannels,room);
                break;
            case ProtocolConstant.CID_GAME_VOTE:  //投票
                room = gameService.checkAndGetRoom(roomId);
                if (room == null) {
                    return;
                }
                gameInfo = gameService.getGameInfo(roomId);
                gameInfo.putVoteInfo(code);
                doVoteTimer(gameInfo,1000*10,Config.VOTE_TYPE_COMMON,roomChannels,room);
                break;
            case ProtocolConstant.CID_GAME_REQ_DARK://要求进入天黑
                responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_DARK,
                        0, 0);
                roomChannels.forEach((id, chan) -> msgGourp.put(chan, responseBody));
                IMBusinessManager.sendGroup(msgGourp);
                break;




        }

    }

    /**
     * 天亮计时器线程
     *
     * @param roomId 房间id
     * @param bout   第几天
     * @param time   计时时间（毫秒）
     */
    private void doDawnTimer(int roomId, int bout, long time) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                doDawn(roomId, bout);
            }
        }, time);
    }

    /**
     * 投票计时器线程
     *
     * @param gameInfo   房间信息
     * @param time   计时时间（毫秒）
     */
    private void doVoteTimer(GameInfo gameInfo, long time,int type,  Map<Integer, UserChannel> channelMap,Room room) {
        Map<UserChannel, ResponseBody> msgGourp = new HashMap<>();
        int bout = room.getBout();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
               if(type==Config.VOTE_TYPE_CHIEF){
                   if(gameInfo.isChiefVote()){
                       return;
                   }
                   int actionPos =  gameInfo.getVoteWinner();
                   room.setChief(actionPos);
                   ResponseBody responseBody = new ResponseBody(ProtocolConstant.SID_GAME,ProtocolConstant.CID_GAME_ELECT_CHIEF_RESP,0,actionPos);
                   channelMap.forEach((id,chan)-> msgGourp.put(chan,responseBody));
                   IMBusinessManager.sendGroup(msgGourp);
                   gameInfo.setChiefVote(true);
               }
               else if(type==Config.VOTE_TYPE_COMMON){
                   if(gameInfo.getIsVote(bout)){
                       return;
                   }
                   int votePos =  gameInfo.getVoteWinner();
                   room.updateLiveList(votePos);
                   ResponseBody responseBody = new ResponseBody(ProtocolConstant.SID_GAME,ProtocolConstant.CID_GAME_VOTE_RESP,0,votePos);
                   channelMap.forEach((id,chan)-> msgGourp.put(chan,responseBody));
                   IMBusinessManager.sendGroup(msgGourp);
                   gameInfo.setIsVote(bout,true);
               }
            }
        }, time);
    }


    private void doDawn(int roomId, int bout) {
        Map<UserChannel, ResponseBody> msgGourp = new HashMap<>();
        Map<Integer, UserChannel> roomChannels = userService.getByRoomId(roomId);

        Room room = gameService.checkAndGetRoom(roomId);
        if (room == null) {
            return;
        }
        List<Integer> deadList = room.getGameInfoMap().get(bout).getDeadList();
        int over = GameUtil.isGameOver(room.getPlayers());
        Map<String, Object> param = new HashMap<>();
        ResponseBody responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_DAWN,
                0, over);
        param.put("bout", bout + 1);
        if (deadList.size() > 0) {
            param.put("killed", deadList);
        }
        room.updateLiveList(deadList.toArray(new Integer[0]));
        responseBody.setParam(param);
        roomChannels.forEach((id, chan) -> msgGourp.put(chan, responseBody));
        IMBusinessManager.sendDawnMsg(msgGourp);
        room.setBout(bout + 1);

    }


    /**
     * 获取某个房间所有狼人channel
     *
     * @param roomChannels 某房间用户channel集合
     * @param players      某房间用户角色信息
     * @return 某个房间所有狼人channel
     */
    private Map<Integer, UserChannel> getWolfs(Map<Integer, UserChannel> roomChannels, Map<Integer, UserRole> players) {
        Map<Integer, UserChannel> wolfsChannel = new HashMap<>();
        roomChannels.forEach((id, userChan) -> {
            if (players.get(id).getRoleId() == Config.ROLE_CODE_OF_WOLF) {
                wolfsChannel.put(id, userChan);
            }
        });
        return wolfsChannel;
    }

    private Map<Integer, UserChannel> getRoleMapById(Map<Integer, UserChannel> roomChannels, Map<Integer, UserRole> players, int roleId) {
        Map<Integer, UserChannel> roleChannel = new HashMap<>();
        roomChannels.forEach((id, userChan) -> {
            if (players.get(id).getRoleId() == roleId) {
                roleChannel.put(id, userChan);
            }
        });
        return roleChannel;
    }


}
