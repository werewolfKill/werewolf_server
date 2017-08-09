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
import com.zinglabs.zwerewolf.service.GameService;
import com.zinglabs.zwerewolf.service.UserService;
import com.zinglabs.zwerewolf.util.ByteBufUtil;
import com.zinglabs.zwerewolf.util.GameUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<UserChannel,ResponseBody> msgGourp = new HashMap<>();
        Map<Integer, UserChannel> roomChannels = userService.getByRoomId(roomId);

        Room room = null;
        GameInfo gameInfo;
        ResponseBody responseBody;
        UserChannel userChannel;

        switch (commandId) {
            case ProtocolConstant.CID_GAME_READY_REQ:  //准备、离开游戏
                int ready = gameService.readyGame(fromId, roomId);
                ResponseBody readyBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_READY_RESP,
                        fromId, ready);
                roomChannels.forEach((id, chan) -> msgGourp.put(chan,readyBody));
                IMBusinessManager.sendGroup(msgGourp);
                break;
            case ProtocolConstant.CID_GAME_START_REQ: //开始游戏
                room = gameService.checkAndGetRoom(roomId, fromId);
                if (room == null) {    //房间为空
                    responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_START_FAIL,
                            fromId, Config.ROOM_NOT_EXIST);
                    userChannel = roomChannels.get(fromId);
                    msgGourp.put(userChannel,responseBody);
                } else if (room.getCurNumber() < room.getNumber()) {   //人数不够
                    responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_START_FAIL,
                            fromId, Config.ROOM_NOT_ENOUGH_NUM);
                    userChannel = roomChannels.get(fromId);
                    msgGourp.put(userChannel,responseBody);
                } else {                      //开始游戏
                    Map<Integer, UserRole> roleMap = gameService.allotRoles(room);
                    room.getGameInfoMap().put(1,new NightInfo());
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
                        msgGourp.put(uchan,resBody);
                    });
                }
                IMBusinessManager.sendStartMsg(msgGourp);
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
                wolfChannel.forEach((id, chan) -> msgGourp.put(chan,responseBody));
                IMBusinessManager.sendGroup(msgGourp);  //告知同伴自己击杀的人
                int wolfSize = wolfChannel.size();

                if (wolfSize == gameInfo.getKillNumber() || gameInfo.getKillNumber() == 1 /* 测试*/) {
                    int killed = gameInfo.getKilled();  //得到被杀的人
                    room.getGameInfoMap().get(bout).setKillId(code);
                    room.addKilled(killed);

                    Witch witch = (Witch) GameUtil.getRoleFromRoleMap(room.getPlayers(), Config.ROLE_CODE_OF_WITCH);
                    if (witch.getSaveId() > 0) {  //已用过解药
                        break;
                    }
                    //向女巫告知杀人信息
                    ResponseBody killBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_NOTIFY_WITCH_KILLED,
                            0, killed);
                    Map<Integer, UserChannel> witchChannel = getRoleMapById(roomChannels, room.getPlayers(), Config.ROLE_CODE_OF_WITCH);
                    msgGourp.clear();
                    witchChannel.forEach((id, chan) -> msgGourp.put(chan,killBody));
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
                msgGourp.put(userChannel,responseBody);
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
            case ProtocolConstant.CID_GAME_TIMER_OVER://计时时间到,天亮了
                room = gameService.checkAndGetRoom(roomId);
                if (room == null) {
                    return;
                }
                List<Integer> deadList = room.getGameInfoMap().get(bout).getDeadList();
                int over = GameUtil.isGameOver(room.getPlayers());
                Map<String, Object> param = new HashMap<>();
                responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_DAWN,
                        0, over);
                if (deadList.size() > 0) {
                    param.put("killed", deadList);
                    responseBody.setParam(param);
                }
                roomChannels.forEach((id, chan) -> msgGourp.put(chan,responseBody));
                IMBusinessManager.sendDawnMsg(msgGourp);
                break;
            case ProtocolConstant.CID_GAME_ASK_CHIEF: //请求竞选警长
                room = gameService.checkAndGetRoom(roomId);
                if (room == null) {
                    return;
                }
                gameInfo = gameService.getGameInfo(roomId);
                if (gameInfo == null) {
                    gameInfo = new GameInfo();
                }

                responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_ASK_CHIEF_RESP,
                        fromId, fromId);
                roomChannels.forEach((id, chan) -> msgGourp.put(chan,responseBody));
                IMBusinessManager.sendGroup(msgGourp);
                if (gameInfo.getVotePoliceNum() > 8) {   //测试
                    int rid = gameInfo.getChiefVotes().get(0);  //随机选取一人
                    ResponseBody voteBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_POLICE_SPEAKING,
                            fromId, rid);
                    roomChannels.forEach((id, chan) -> msgGourp.put(chan,voteBody));
                    IMBusinessManager.sendGroup(msgGourp);
                }
                break;
            case ProtocolConstant.CID_GAME_QUIT_POLICE : //取消竞选警长
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
                roomChannels.forEach((id, chan) -> msgGourp.put(chan,responseBody));
                IMBusinessManager.sendGroup(msgGourp);
                break;
            case ProtocolConstant.CID_GAME_CHIEF_VOTE:  //警上投票
                room = gameService.checkAndGetRoom(roomId);
                if (room == null) {
                    return;
                }
                

        }

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
