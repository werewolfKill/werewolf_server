package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.config.Config;
import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.entity.Room;
import com.zinglabs.zwerewolf.entity.UserChannel;
import com.zinglabs.zwerewolf.entity.RequestBody;
import com.zinglabs.zwerewolf.entity.ResponseBody;
import com.zinglabs.zwerewolf.entity.role.UserRole;
import com.zinglabs.zwerewolf.im.IMChannelGroup;
import com.zinglabs.zwerewolf.manager.IMBusinessManager;
import com.zinglabs.zwerewolf.service.BusinessService;
import com.zinglabs.zwerewolf.service.UserService;
import com.zinglabs.zwerewolf.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangtonghe
 * @date 2017/7/24 21:24
 */
public class BusinessController implements BaseController {
    private BusinessService businessService = new BusinessService();

    private UserService userService = new UserService();

    @Override
    public void doDestory(Map application) {

    }

    @Override
    public void doAccept(short commandId, Channel channel, ByteBuf body, Map<String, Object> application) {
        RequestBody requestBody = ByteBufUtil.resolveBusiness(body);
        int fromId = requestBody.getFromId();
        int code = requestBody.getCode();
        Map<UserChannel, ResponseBody> msgGourp = new HashMap<>();
        ResponseBody responseBody = null;

        switch (commandId) {
            case ProtocolConstant.CID_BNS_CRE_ROOM_REQ:  //创建房间，code为模式Id
                Room creRoom = businessService.createRoom(fromId, code);
                responseBody = new ResponseBody(ProtocolConstant.SID_BNS, ProtocolConstant.CID_BNS_CRE_ROOM_RESP,
                        fromId, creRoom.getId());
                Map<String, Object> roomMap = new HashMap<>();
                roomMap.put("room", creRoom);
                responseBody.setParam(roomMap);
                UserChannel userChannel = userService.getByUserId(fromId);
                msgGourp.put(userChannel, responseBody);
                IMBusinessManager.sendRoomMsg(msgGourp);
                break;
            case ProtocolConstant.CID_BNS_FIND_ROOM_REQ:  //搜索房间并进入，code为要搜索的房间号
                Room serRoom = businessService.searchRoom(fromId, code);
                if (serRoom == null) {                //房间不存在
                    responseBody = new ResponseBody(ProtocolConstant.SID_BNS, ProtocolConstant.CID_BNS_FIND_ROOM_RESP,
                            fromId, Config.ROOM_NOT_EXIST);
                    UserChannel chan = userService.getByUserId(fromId);
                    msgGourp.put(chan, responseBody);

                } else if (serRoom.enterRoom(fromId)) {     //房间进入成功,通知房间所有人
                    int position = serRoom.getPlayers().get(fromId).getPosition();
                    ResponseBody enterBody = new ResponseBody(ProtocolConstant.SID_BNS, ProtocolConstant.CID_BNS_FIND_ROOM_RESP,
                            fromId, Config.ROOM_SEARCH_SUCCESS);  //对自己响应，进入房间
                    ResponseBody otherBody = new ResponseBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_OTHER_ENTER,
                            fromId, position);  // 对其他人，有人进入房间
                    Map<String, Object> param = new HashMap<>();
                    param.put("room", serRoom);
                    enterBody.setParam(param);
//                    otherBody.
                    Map<Integer, UserChannel> roomChannels = userService.getByRoomId(code);
                    roomChannels.forEach((id, chan) -> {
                        if (id == fromId) {
                            msgGourp.put(chan, enterBody);
                        } else {
                            msgGourp.put(chan, otherBody);
                        }
                    });
                } else {
                    responseBody = new ResponseBody(ProtocolConstant.SID_BNS, ProtocolConstant.CID_BNS_FIND_ROOM_RESP,
                            fromId, Config.ROOM_ALREADY_FULL);  //房间已满
                    UserChannel chan = userService.getByUserId(fromId);
                    msgGourp.put(chan, responseBody);
                }
                IMBusinessManager.sendRoomMsg(msgGourp);
                break;
        }
    }
}
