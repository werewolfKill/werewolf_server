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
import java.util.concurrent.*;

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
        room = gameService.checkAndGetRoom(roomId);
        if (room == null) {
            return;
        }
        if (bout < room.getBout()) {
            return;
        }
        switch (commandId) {
            case ProtocolConstant.CID_GAME_READY_REQ:  //准备、离开游戏

                int ready = gameService.readyGame(fromId, roomId);
                ResponseBody readyBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_READY_RESP,
                        fromId, ready);
                roomChannels.forEach((id, chan) -> msgGourp.put(chan, readyBody));
                IMBusinessManager.sendGroup(msgGourp);
                testReadyTimer(roomId, 2000, room);
                break;
            case ProtocolConstant.CID_GAME_START_REQ: //开始游戏
                room = gameService.checkAndGetRoom(roomId, fromId);
                if (room == null) {    //房间为空
                    responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_START_FAIL,
                            fromId, Config.ROOM_NOT_EXIST);
                    userChannel = roomChannels.get(fromId);
                    msgGourp.put(userChannel, responseBody);
                    IMBusinessManager.sendStartMsg(msgGourp);
                } else if (room.getCurNumber() < room.getNumber()) {   //人数不够
                    responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_START_FAIL,
                            fromId, Config.ROOM_NOT_ENOUGH_NUM);
                    userChannel = roomChannels.get(fromId);
                    msgGourp.put(userChannel, responseBody);
                    IMBusinessManager.sendStartMsg(msgGourp);
                } else if (!gameService.isAllReady(room)) {   //有人没准备好
                    responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_START_FAIL,
                            fromId, Config.ROOM_NOT_ALL_READY);
                    userChannel = roomChannels.get(fromId);
                    msgGourp.put(userChannel, responseBody);
                    IMBusinessManager.sendStartMsg(msgGourp);
                } else {                     //开始游戏
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
                    IMBusinessManager.sendStartMsg(msgGourp);
                    doDawnTimer(roomId, bout, 60 * 1000);
                }
                break;
            case ProtocolConstant.CID_GAME_KILL_REQ:   //狼人杀人
                Room wolfRoom = gameService.checkAndGetRoom(roomId);
                if (wolfRoom == null) {
                    return;
                }
                GameInfo killInfo = gameService.getGameInfo(roomId);
                responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_KILL_RESP,
                        fromId, code);  //code表示要杀的人的id
                killInfo.putKillInfo(code);
                Map<Integer, UserChannel> wolfChannel = getWolfs(roomChannels, wolfRoom.getPlayers());
                wolfChannel.forEach((id, chan) -> msgGourp.put(chan, responseBody));
                IMBusinessManager.sendGroup(msgGourp);  //告知同伴自己击杀的人
                Boolean isSendKill = killInfo.getIsKill(bout);
                if (isSendKill != null && isSendKill) {
                    return;
                }
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        Boolean isSendKill = killInfo.getIsKill(bout);
                        if (isSendKill != null && isSendKill) {
                            return;
                        }
                        int killed = killInfo.getKilled();  //得到被杀的人
                        wolfRoom.getGameInfoMap().get(bout).setKillId(code);
                        wolfRoom.addKilled(killed);

                        Witch witch = (Witch) GameUtil.getRoleFromRoleMap(wolfRoom.getPlayers(), Config.ROLE_CODE_OF_WITCH);
                        if (witch != null && witch.getSaveId() > 0) {  //已用过解药
                            return;
                        }
                        //向女巫告知杀人信息
                        ResponseBody killBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_NOTIFY_WITCH_KILLED,
                                0, killed);
                        Map<Integer, UserChannel> witchChannel = getRoleMapById(roomChannels, wolfRoom.getPlayers(), Config.ROLE_CODE_OF_WITCH);
                        msgGourp.clear();
                        witchChannel.forEach((id, chan) -> msgGourp.put(chan, killBody));
                        IMBusinessManager.sendGroup(msgGourp);
                        killInfo.setIsKill(bout, true);
                    }
                }, 30 * 1000);
                break;
            case ProtocolConstant.CID_GAME_SAVE_REQ:  //女巫救人请求
                Witch saveWitch = (Witch) room.getPlayers().get(fromId).getRole();
                if (saveWitch.getSaveId() == 0) {
                    saveWitch.setSaveId(code);
                    room.getGameInfoMap().get(bout).setSaveId(code);
                }
                break;
            case ProtocolConstant.CID_GAME_POISON_REQ:  //女巫毒人请求
                Witch pwitch = (Witch) room.getPlayers().get(fromId).getRole();
                if (pwitch.getPoisonId() == 0) {
                    room.getGameInfoMap().get(bout).setPoisonId(code);
                    pwitch.setPoisonId(code);
                }
                break;
            case ProtocolConstant.CID_GAME_VERIFY_REQ:  //预言家验人
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
                Guard guard = (Guard) room.getPlayers().get(fromId).getRole();
                if (guard.setGuardian(code)) {
                    room.getGameInfoMap().get(bout).setGuardianId(code);
                }
                break;
            case ProtocolConstant.CID_GAME_ASK_CHIEF: //请求竞选警长
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
                        if (chiefInfo.isChiefSpeak()) {
                            return;
                        }
                        int size = chiefInfo.getChiefVotes().size();
                        int rid = chiefInfo.getChiefVotes().get(new Random().nextInt(size)); //随机选取一人
                        ResponseBody voteBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_POLICE_START_SPEAKING,
                                fromId, rid);
                        roomChannels.forEach((id, chan) -> msgGourp.put(chan, voteBody));
                        chiefInfo.setChiefSpeak(true);
                        IMBusinessManager.sendGroup(msgGourp);
                    }
                }, 1000 * 30);
                break;
            case ProtocolConstant.CID_GAME_QUIT_POLICE: //取消竞选警长
                gameInfo = gameService.getGameInfo(roomId);
                if (gameInfo == null) {
                    gameInfo = new GameInfo();
                }
                if (code == 0) {
                    break;
                }
                gameInfo.addQuitChiefs(room.getPlayers().get(fromId).getPosition());
                responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_QUIT_POLICE_RESP,
                        fromId, fromId);
                roomChannels.forEach((id, chan) -> msgGourp.put(chan, responseBody));
                IMBusinessManager.sendGroup(msgGourp);
                break;
            case ProtocolConstant.CID_GAME_POLICE_SPEAKING_END:  //警上发言结束
                gameInfo = gameService.getGameInfo(roomId);
                responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_START_CHIEF_VOTE,
                        fromId, fromId);
                List<Integer> chiefList = gameInfo.getChiefVotes();
                List<Integer> quitList = gameInfo.getQuitChiefs();
                gameInfo.addSpeakEndNum();
                if (gameInfo.getSpeakEndNum() == gameInfo.getVotePoliceNum()) {
                    roomChannels.forEach((id, chan) -> {
                        if (!chiefList.contains(id) || chiefList.contains(id) && quitList.contains(id)) {  //向未上警玩家发请求
                            msgGourp.put(chan, responseBody);
                        }
                    });
                    IMBusinessManager.sendGroup(msgGourp);
                }
                break;
            case ProtocolConstant.CID_GAME_CHIEF_VOTE:  //警上投票
                gameInfo = gameService.getGameInfo(roomId);
                gameInfo.putVoteInfo(code);
                doVoteTimer(gameInfo, 1000 * 15, Config.VOTE_TYPE_CHIEF, roomChannels, room);

                break;
            case ProtocolConstant.CID_GAME_CHIEF_DECIDE_SPEAK: //开始发言
                int actionPos;

                //上一夜死亡名单
                List<Integer> list = room.getGameInfoMap().get(bout - 1).getDeadList();
                if (list.size() > 0) {
                    actionPos = list.get(0);
                } else {
                    actionPos = room.getChief();
                }
                int speakPos = GameUtil.decideSpeak(room,actionPos,code==0);

                responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_START_SPEAKING,
                        0, speakPos);
                roomChannels.forEach((id, chan) -> msgGourp.put(chan, responseBody));
                IMBusinessManager.sendGroup(msgGourp);
                break;
            case ProtocolConstant.CID_GAME_REQ_VOTE:
                Integer chief = room.getChief();
                gameInfo = gameService.getGameInfo(roomId);
                gameInfo.addAskVote(bout);
//                if (gameInfo.getAskVote(bout) < room.getLiveList().size()) {  //请求投票数需为存活人数
//                    return;
//                }
                if (gameInfo.getAskVote(bout) < 3) {  //测试
                    return;
                }
                if (chief > 0) {
                    responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_CHIEF_REQ_SUM_TICKET,
                            0, 0);
                    int chiefId = GameUtil.getIdByPos(room.getPlayers(), chief);
                    userChannel = roomChannels.get(chiefId);
                    msgGourp.put(userChannel, responseBody);
                    IMBusinessManager.sendGroup(msgGourp);
                } else {
                    responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_REQ_VOTE_RESP,
                            0, 0);
                    roomChannels.forEach((id, chan) -> msgGourp.put(chan, responseBody));
                    IMBusinessManager.sendGroup(msgGourp);
                }
                break;
            case ProtocolConstant.CID_GAME_CHIEF_SUM_TICKET://警长归票
                responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_REQ_VOTE_RESP,
                        0, code);
                roomChannels.forEach((id, chan) -> msgGourp.put(chan, responseBody));
                IMBusinessManager.sendGroup(msgGourp);

                gameInfo = gameService.getGameInfo(roomId);
                doVoteTimer(gameInfo, 1000 * 15, Config.VOTE_TYPE_COMMON, roomChannels, room);
                break;
            case ProtocolConstant.CID_GAME_VOTE:  //投票
                gameInfo = gameService.getGameInfo(roomId);
                gameInfo.putVoteInfo(code);
                doVoteTimer(gameInfo, 1000 * 30, Config.VOTE_TYPE_COMMON, roomChannels, room);
                break;
            case ProtocolConstant.CID_GAME_REQ_DARK://要求进入天黑

                if (room.getGameInfoMap().get(bout) != null && room.getGameInfoMap().get(bout).isAskNight()) {
                    return;
                }
                room.getGameInfoMap().get(bout).setAskNight(true);  //设置天黑
                responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_DARK,
                        0, 0);
                roomChannels.forEach((id, chan) -> msgGourp.put(chan, responseBody));
                IMBusinessManager.sendGroup(msgGourp);

                doDawnTimer(roomId, bout, 60 * 1000);
                break;
            case ProtocolConstant.CID_GAME_CHANGE_CHIEF: //移交警徽
                if (room.getGameInfoMap().get(bout) != null && room.getGameInfoMap().get(bout).isAskNight()) {
                    return;
                }
                room.setChief(code);
                responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_CHANGE_CHIEF_RESP,
                        fromId, code);
                roomChannels.forEach((id, chan) -> msgGourp.put(chan, responseBody));
                IMBusinessManager.sendGroup(msgGourp);
                break;
        }

    }


    public void testReadyTimer(int roomId, long time, Room room) {
        Map<UserChannel, ResponseBody> msgGourp = new HashMap<>();
        Map<Integer, UserChannel> roomChannels = userService.getByRoomId(roomId);
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        Map<Integer, UserRole> players = room.getPlayers();
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(players.size());
        List<Integer> idList = new ArrayList<>();
        players.forEach((id, ur) -> {
            if (!ur.isReady() && room.getOwner() != id) {
                idList.add(id);
            }
        });
        queue.addAll(idList);
        service.scheduleAtFixedRate(() -> {
            Integer fromId = queue.poll();
            if (fromId == null) {
                service.shutdown();
                return;
            }
            int ready = 1;
            players.get(fromId).setReady(true);
            ResponseBody readyBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_READY_RESP,
                    fromId, ready);
            roomChannels.forEach((id, chan) -> msgGourp.put(chan, readyBody));
            IMBusinessManager.sendGroup(msgGourp);

        }, time, time, TimeUnit.MILLISECONDS);
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
     * @param gameInfo 房间信息
     * @param time     计时时间（毫秒）
     */
    private void doVoteTimer(GameInfo gameInfo, long time, int type, Map<Integer, UserChannel> channelMap, Room room) {
        Map<UserChannel, ResponseBody> msgGourp = new HashMap<>();
        int bout = room.getBout();
        if (type == Config.VOTE_TYPE_COMMON) {
            Boolean isVote = gameInfo.getIsVote(bout);
            if (isVote != null && isVote) {
                return;
            }
        } else {
            if (gameInfo.isChiefVote()) {
                return;
            }
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (type == Config.VOTE_TYPE_CHIEF) {
                    if (gameInfo.isChiefVote()) {
                        return;
                    }
                    int actionPos = gameInfo.getVoteWinner();
                    room.setChief(actionPos);
                    ResponseBody responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_ELECT_CHIEF_RESP, 0, actionPos);
                    channelMap.forEach((id, chan) -> msgGourp.put(chan, responseBody));
                    IMBusinessManager.sendGroup(msgGourp);
                    gameInfo.setChiefVote(true);
                } else if (type == Config.VOTE_TYPE_COMMON) {
                    Boolean isVote = gameInfo.getIsVote(bout);
                    if (isVote != null && isVote) {
                        return;
                    }
                    int votePos = gameInfo.getVoteWinner();
                    room.updateLiveList(votePos);
                    ResponseBody responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_VOTE_RESP, 0, votePos);
                    channelMap.forEach((id, chan) -> msgGourp.put(chan, responseBody));
                    IMBusinessManager.sendGroup(msgGourp);
                    gameInfo.setIsVote(bout, true);
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
        if (bout < room.getBout()) {
            return;
        }
        room.setBout(bout + 1);
        room.getGameInfoMap().put(bout + 1, new NightInfo());
        List<Integer> deadList = room.getGameInfoMap().get(bout).getDeadList();
        room.updateLiveList(deadList.toArray(new Integer[0]));
        int over = GameUtil.isGameOver(room.getPlayers());
        Map<String, Object> param = new HashMap<>();
        ResponseBody responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_DAWN,
                0, over);
        param.put("bout", bout + 1);
        if (deadList.size() > 0) {
            param.put("killed", deadList);
        }
        responseBody.setParam(param);
        roomChannels.forEach((id, chan) -> msgGourp.put(chan, responseBody));
        IMBusinessManager.sendDawnMsg(msgGourp);

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
