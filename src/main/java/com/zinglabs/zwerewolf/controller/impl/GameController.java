package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.config.Config;
import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.entity.*;
import com.zinglabs.zwerewolf.entity.role.UserRole;
import com.zinglabs.zwerewolf.im.IMChannelGroup;
import com.zinglabs.zwerewolf.manager.IMBusinessManager;
import com.zinglabs.zwerewolf.service.GameService;
import com.zinglabs.zwerewolf.service.UserService;
import com.zinglabs.zwerewolf.util.ByteBufUtil;
import com.zinglabs.zwerewolf.util.RoomUtil;
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
        Map<ResponseBody, UserChannel> msgGourp = new HashMap<>();
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
                roomChannels.forEach((id, chan) -> msgGourp.put(readyBody, chan));
                IMBusinessManager.sendGroup(msgGourp);
                break;
            case ProtocolConstant.CID_GAME_START_REQ: //开始游戏
                room = gameService.checkAndGetRoom(roomId, fromId);
                if (room == null) {
                    responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_START_FAIL,
                            fromId, Config.ROOM_NOT_EXIST);
                    userChannel = roomChannels.get(fromId);
                    msgGourp.put(responseBody, userChannel);
                } else if (room.getCurNumber() < room.getNumber()) {
                    responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_START_FAIL,
                            fromId, Config.ROOM_NOT_ENOUGH_NUM);
                    userChannel = roomChannels.get(fromId);
                    msgGourp.put(responseBody, userChannel);
                } else {
                    Map<Integer, UserRole> roleMap = gameService.allotRoles(room);
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
                        msgGourp.put(resBody, uchan);
                    });
                }
                IMBusinessManager.sendStartMsg(msgGourp);
                break;
            case ProtocolConstant.CID_GAME_KILL_REQ:   //狼人杀人
                room = gameService.checkAndGetRoom(roomId, fromId);
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
                wolfChannel.forEach((id, chan) -> msgGourp.put(responseBody, chan));
                IMBusinessManager.sendGroup(msgGourp);  //告知同伴自己击杀的人
                int wolfSize = wolfChannel.size();

                //向女巫告知杀人信息
                if (wolfSize == gameInfo.getKillNumber() || gameInfo.getKillNumber() == 1 /* 测试*/) {
                    int killed = gameInfo.getKilled();  //得到被杀的人
                    if (killed > 0) {
                        room.getPlayers().get(killed).setKilled(true);  //设置死亡信息
                    }
                    ResponseBody killBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_NOTIFY_WITCH_KILLED,
                            0, killed);
                    Map<Integer, UserChannel> witchChannel = getRoleById(roomChannels, room.getPlayers(), Config.ROLE_CODE_OF_WITCH);
                    msgGourp.clear();
                    witchChannel.forEach((id, chan) -> msgGourp.put(killBody, chan));
                    IMBusinessManager.sendGroup(msgGourp);
                }
                break;
            case ProtocolConstant.CID_GAME_SAVE_REQ:  //女巫救人请求
                room = gameService.checkAndGetRoom(roomId, fromId);
                if (room == null) {
                    return;
                }
                room.getPlayers().get(code).setSaved(true);
                break;
            case ProtocolConstant.CID_GAME_POISON_REQ:  //女巫毒人请求
                room = gameService.checkAndGetRoom(roomId, fromId);
                if (room == null) {
                    return;
                }
                room.getPlayers().get(code).setPoisoned(true);
                break;
            case ProtocolConstant.CID_GAME_VERIFY_REQ:  //预言家验人
                room = gameService.checkAndGetRoom(roomId, fromId);
                if (room == null) {
                    return;
                }
                int roleId = room.getPlayers().get(fromId).getRoleId();
                int isGood = RoomUtil.verify(roleId) ? 1 : 0;
                responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_VERIFY_RESP,
                        fromId, isGood);
                userChannel = roomChannels.get(fromId);
                msgGourp.put(responseBody, userChannel);
                IMBusinessManager.sendGroup(msgGourp);
                break;
            case ProtocolConstant.CID_GAME_GUARD_REQ:   //守卫守人
                room = gameService.checkAndGetRoom(roomId, fromId);
                if (room == null) {
                    return;
                }
                room.getPlayers().get(code).setGuarded(true);
                break;
        }

    }

    /**
     * 获取某个房间所有狼人
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

    private Map<Integer, UserChannel> getRoleById(Map<Integer, UserChannel> roomChannels, Map<Integer, UserRole> players, int roleId) {
        Map<Integer, UserChannel> roleChannel = new HashMap<>();
        roomChannels.forEach((id, userChan) -> {
            if (players.get(id).getRoleId() == roleId) {
                roleChannel.put(id, userChan);
            }
        });
        return roleChannel;
    }
}
