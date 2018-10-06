package org.yuanmeng.zwolfserver.server.manager;

import org.yuanmeng.zwolfserver.entity.ProtocolField;
import org.yuanmeng.zwolfserver.entity.Packet;
import org.yuanmeng.zwolfserver.server.im.IMChannelGroup;
import org.yuanmeng.zwolfserver.entity.UserChannel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.Map;

/**
 * 聊天消息转发器
 * author: vector.huang
 * date：2016/4/18 21:01
 */
public class IMMessageManager {

    /**
     * 群聊文字消息转发
     *
     * @param body 消息体
     */
    public static void sendGroupTextReq(ByteBuf body) {
        int fromId = body.readInt();
        byte[] contentByte = new byte[body.readableBytes()];
        //4、复制内容到字节数组b  
        body.readBytes(contentByte);
        Map<Integer, UserChannel> userChannels = IMChannelGroup.instance().getChannels();
        userChannels.forEach((userId, userChannel) -> {
            Channel toChannel = userChannel.getChannel();
            if (toChannel == null) {
                return;
            }
            byte[] fromName = userChannels.get(fromId).getUserName().getBytes();

            ByteBuf msg = toChannel.alloc().buffer(contentByte.length + fromName.length + 12);

            msg.writeInt(fromId);

            msg.writeInt(fromName.length);

            msg.writeBytes(fromName);

            msg.writeInt(contentByte.length);
            msg.writeBytes(contentByte);
            Packet packet = new Packet(msg.readableBytes() + 12, ProtocolField.SID_MSG, ProtocolField.CID_MSG_TEXT_RESP,
                    msg);


            toChannel.writeAndFlush(packet);
            System.out.println("send to  username: %s, msg: %s" + body);
        });
    }

    /**
     * 群聊语音消息转发
     *
     * @param body 消息体
     */
    public static void sendGroupVoiceReq(ByteBuf body) {
        int fromId = body.readInt();
        int audiodataLength = body.readInt();
        byte[] audiodata = new byte[audiodataLength];
        body.readBytes(audiodataLength).readBytes(audiodata);
        //4、复制内容到字节数组b  
        Map<Integer, UserChannel> userChannels = IMChannelGroup.instance().getChannels();
        userChannels.forEach((userId, userChannel) -> {
            if (userId != fromId) {
                Channel toChannel = userChannel.getChannel();
                if (toChannel == null) {
                    return;
                }
                ByteBuf msg = toChannel.alloc().buffer(audiodata.length + 4);

                msg.writeInt(audiodata.length);
                msg.writeBytes(audiodata);
                Packet packet = new Packet(msg.readableBytes() + 12, ProtocolField.SID_MSG, ProtocolField.CID_MSG_VOICE_RESP,
                        msg);
                toChannel.writeAndFlush(packet);
            }
        });
    }


}
