package com.zinglabs.zwerewolf.handler;



import java.util.List;

import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.entity.Packet;
import com.zinglabs.zwerewolf.im.ThreadServerSocket;
import com.zinglabs.zwerewolf.manager.IMMessageManager;
import com.zinglabs.zwerewolf.manager.IMUserManager;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * 登录处理着，只有登录之后的用户才能去到下一个Handler
 * 不然就丢弃全部数据
 * <p>
 * author: vector.huang
 * date：2016/4/18 19:25
 */
public class LoginChannelHandler extends MessageToMessageDecoder<Packet> {

    private int userId;
    private ThreadServerSocket.OnChannelActiveListener listener;

    public LoginChannelHandler(ThreadServerSocket.OnChannelActiveListener listener) {
        this.listener = listener;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        if (listener != null) {
            listener.onChannelActive(ctx);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        if (userId > 0) {
            IMUserManager.logout(userId);
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, Packet packet, List<Object> out) throws Exception {

        System.out.println("packet -- "+packet.getServiceId() + ":"+packet.getCommandId());


        //用户服务
        if (packet.getServiceId() == ProtocolConstant.SID_USER) {
            if (packet.getCommandId() == ProtocolConstant.CID_USER_LOGIN_REQ) {
                userId = IMUserManager.loginReq(ctx.channel(),packet.getBody());
                return;
            }
        }
        
        if(packet.getServiceId() == ProtocolConstant.SID_MSG){
            switch (packet.getCommandId()){
                case ProtocolConstant.CID_MSG_SEND_SINGLE_REQ:
                    IMMessageManager.sendGroupMsgReq(packet.getUserId(),packet.getBody());
                    break;
            }
            return;
        }

        if(userId <= 0){
            //未登录的什么都不做了
            return;
        }

        packet.setUserId(userId);
        out.add(packet);
    }
}
