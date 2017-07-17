package com.vector.im.handler;

import com.vector.im.config.Config;
import com.vector.im.constant.ProtocolConstant;
import com.vector.im.entity.Packet;
import com.vector.im.im.IMChannelGroup;
import com.vector.im.im.ThreadServerSocket;
import com.vector.im.manager.IMLoginManager;
import com.vector.im.manager.IMMessageManager;
import com.vector.im.manager.IMTestManager;
import com.vector.im.manager.IMUserManager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 *
 * 代表一个Channel连接，保存着
 *
 * author: vector.huang
 * date：2016/4/18 19:25
 */
public class PacketChannelHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Packet packet = (Packet) msg;

        //用户服务
        if(packet.getServiceId() == ProtocolConstant.SID_USER){
            switch (packet.getCommandId()){
                case ProtocolConstant.CID_USER_ONLINE_REQ:
                    IMUserManager.onlineUserReq(ctx.channel(),packet.getBody());
                    break;
            }
            return;
        }

        //消息服务
        if(packet.getServiceId() == ProtocolConstant.SID_MSG){
            switch (packet.getCommandId()){
                case ProtocolConstant.CID_MSG_SEND_SINGLE_REQ:
                    IMMessageManager.sendSingleMsgReq(packet.getUserId(),packet.getBody());
                    break;
            }
            return;
        }

    }
}
