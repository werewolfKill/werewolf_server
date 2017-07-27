package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.entity.UserChannel;
import com.zinglabs.zwerewolf.entity.RequestBody;
import com.zinglabs.zwerewolf.entity.ResponseBody;
import com.zinglabs.zwerewolf.im.IMChannelGroup;
import com.zinglabs.zwerewolf.manager.IMBusinessManager;
import com.zinglabs.zwerewolf.service.BusinessService;
import com.zinglabs.zwerewolf.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangtonghe
 * @date 2017/7/24 21:24
 */
public class BusinessController implements BaseController{
    private BusinessService businessService = new BusinessService();
    @Override
    public void doDestory(Map application) {

    }

    @Override
    public void doAccept(short commandId, Channel channel, ByteBuf body, Map<String,Object> application) {
        RequestBody requestBody = ByteBufUtil.encodeBusiness(body);
        int fromId = requestBody.getFromId();
        int content = (Integer) requestBody.getContent();
        Map<ResponseBody,UserChannel> msgGourp = new HashMap<>();


        switch (commandId) {
            case ProtocolConstant.CID_BNS_CRE_ROOM_REQ:  //创建房间
                int roomId =businessService.createRoom(fromId,1);
                ResponseBody responseBody = new ResponseBody(ProtocolConstant.SID_BNS, ProtocolConstant.CID_BNS_CRE_ROOM_RESP,
                        fromId,  roomId);
                Map<Integer, UserChannel> channels = IMChannelGroup.instance().getChannels();
                UserChannel userChannel = channels.get(fromId);
                msgGourp.put(responseBody,userChannel);
                IMBusinessManager.sendGroup(msgGourp);
                break;

        }
    }
}
