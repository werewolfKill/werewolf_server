package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.config.Config;
import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.entity.RequestBody;
import com.zinglabs.zwerewolf.entity.ResponseBody;
import com.zinglabs.zwerewolf.entity.Room;
import com.zinglabs.zwerewolf.entity.UserChannel;
import com.zinglabs.zwerewolf.entity.role.UserRole;
import com.zinglabs.zwerewolf.im.IMChannelGroup;
import com.zinglabs.zwerewolf.manager.IMBusinessManager;
import com.zinglabs.zwerewolf.service.GameService;
import com.zinglabs.zwerewolf.util.ByteBufUtil;
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
        Room room;
        ResponseBody responseBody;
        UserChannel userChannel;

        switch (commandId) {
            case ProtocolConstant.CID_GAME_READY_REQ:  //准备游戏

                ResponseBody readyBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_READY_RESP,
                        fromId, 0);
                //TODO 根据房间号搜索用户
                Map<Integer, UserChannel> readyChannels = IMChannelGroup.instance().getChannels();
                readyChannels.forEach((id, chan) -> {
                    msgGourp.put(readyBody, chan);
                });
                IMBusinessManager.sendGroup(msgGourp);

                break;
            case ProtocolConstant.CID_GAME_START_REQ: //开始游戏
                Map<Integer, UserChannel> startChannels = IMChannelGroup.instance().getChannels();
                room = gameService.checkRoom(roomId, fromId);
                if (room == null) {
                    responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_START_FAIL,
                            fromId, Config.ROOM_NOT_EXIST);
                    userChannel = startChannels.get(fromId);
                    msgGourp.put(responseBody, userChannel);
                } else if (room.getCurNumber() < room.getNumber()) {
                    responseBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_START_FAIL,
                            fromId, Config.ROOM_NOT_ENOUGH_NUM);
                    userChannel = startChannels.get(fromId);
                    msgGourp.put(responseBody, userChannel);
                } else {
                    Map<Integer,UserRole> roleMap = gameService.allotRoles(room);
                    List<Integer> wolfs =new ArrayList<>();
                    roleMap.forEach((id,ur)->{
                        if(ur.getRoleId()==Config.ROLE_CODE_OF_WOLF){
                            wolfs.add(ur.getUserId());
                        }
                    });
                    roleMap.forEach((id,ur)->{
                        UserChannel uchan = startChannels.get(id);
                        ResponseBody resBody  = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_START_RESP,
                                fromId,ur.getRoleId());
                        Map<String,Object> param = new HashMap<>();
                        param.put("wolfs",wolfs);
                        if(ur.getRoleId()==Config.ROLE_CODE_OF_WOLF){
                            resBody.setParam(param);
                        }
                        msgGourp.put(resBody,uchan);
                    });
                }
                IMBusinessManager.sendStartMsg(msgGourp);
                break;
            case ProtocolConstant.CID_GAME_KILL_REQ:   //狼人杀人
                //向所有狼人发送杀人信息
                ResponseBody killBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_KILL_RESP,
                        fromId, code);
                //模拟所有玩家用户  //TODO 根据房间号搜索狼人
                Map<Integer, UserChannel> userChannels = IMChannelGroup.instance().getChannels();
                userChannels.forEach((userId, chan) -> {
                    if (userId % 2 == 1) {
                        msgGourp.put(killBody, chan);
                    }
                });
                IMBusinessManager.sendGroup(msgGourp);
                break;
        }

    }
}
