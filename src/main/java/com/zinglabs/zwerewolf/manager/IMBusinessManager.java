package com.zinglabs.zwerewolf.manager;

import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.entity.BNSMsgBody;
import com.zinglabs.zwerewolf.entity.Packet;
import com.zinglabs.zwerewolf.entity.UserChannel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;

/**
 * 业务消息转发器
 *
 * @author wangtonghe
 * @date 2017/7/24 08:58
 */
public class IMBusinessManager {

    /**
     * 转发发业务消息，通用方法
     * @param body    消息体
     * @param senders 发送者集合
     */
    public static void sendGroup(BNSMsgBody body, Map<Integer,UserChannel> senders) {

        senders.forEach((userId,userChannel) -> {

            Channel toChannel = userChannel.getChannel();
            ByteBuf byteBuf = toChannel.alloc().buffer(16);  //4个int
            byteBuf.writeInt(body.getFromId());
            byteBuf.writeInt(16);
            byteBuf.writeInt(body.getRoomId());
            byteBuf.writeInt(body.getFromId());

            Packet packet = new Packet(12,body.getServiceId(),body.getServiceId(),byteBuf);
            userChannel.getChannel().writeAndFlush(packet);
        });

    }


}
