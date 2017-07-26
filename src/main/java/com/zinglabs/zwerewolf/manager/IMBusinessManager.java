package com.zinglabs.zwerewolf.manager;

import com.zinglabs.zwerewolf.entity.ResponseBody;
import com.zinglabs.zwerewolf.entity.Packet;
import com.zinglabs.zwerewolf.entity.UserChannel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.Map;

/**
 * 业务消息转发器
 *
 * @author wangtonghe
 * @date 2017/7/24 08:58
 */
public class IMBusinessManager {

    /**
     * 转发业务消息，通用方法
     * @param body    消息体
     * @param senders 发送者集合
     */
    public static void sendGroup(ResponseBody body, Map<Integer,UserChannel> senders) {

        senders.forEach((userId,userChannel) -> {

            Channel toChannel = userChannel.getChannel();
            ByteBuf byteBuf = toChannel.alloc().buffer(8);  //2个int
            byteBuf.writeInt(body.getFromId());
            byteBuf.writeInt(body.getReply());

            Packet packet = new Packet(12+byteBuf.readableBytes(),body.getServiceId(),body.getCommand(),byteBuf);
            userChannel.getChannel().writeAndFlush(packet);
        });

    }


}
