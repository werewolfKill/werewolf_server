package com.vector.im.manager;

import com.vector.im.constant.ProtocolConstant;
import com.vector.im.entity.Packet;
import com.vector.im.im.IMChannelGroup;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * author: vector.huang
 * date：2016/4/18 21:01
 */
public class IMMessageManager {

    /**
     * 单聊
     */
    public static void sendSingleMsgReq(int from,ByteBuf body){
        int to = body.readInt();
        Channel toChannel = IMChannelGroup.instance().get(to).getChannel();
        if(toChannel == null){
            return;
        }

        ByteBuf msg = toChannel.alloc().buffer();
        msg.writeInt(from);
        msg.writeBytes(body);
        Packet packet = new Packet(msg.readableBytes()+12, ProtocolConstant.SID_MSG,ProtocolConstant.CID_MSG_RECEIVE_SINGLE_OUT,
                msg);
        toChannel.writeAndFlush(packet);
    }

}
