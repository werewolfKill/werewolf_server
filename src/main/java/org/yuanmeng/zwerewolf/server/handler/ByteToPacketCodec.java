package org.yuanmeng.zwerewolf.server.handler;


import java.util.List;

import org.yuanmeng.zwerewolf.server.entity.Packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

/**
 *
 * ByteBuf 与 Packet 之间的编码解码
 *
 * author: vector.huang
 * date：2016/4/18 19:18
 */
public class ByteToPacketCodec extends ByteToMessageCodec<Packet>{

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getLength())
                .writeShort(msg.getServiceId())
                .writeShort(msg.getCommandId())
                .writeShort(msg.getVersion())
                .writeShort(msg.getReserved())
                .writeBytes(msg.getBody());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        Packet packet = new Packet(
                in.readInt(),
                in.readShort(),
                in.readShort(),
                in.readShort(),
                in.readShort(),
                in.readBytes(in.readableBytes())
        );
        out.add(packet);
    }
}
