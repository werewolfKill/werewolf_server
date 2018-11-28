package org.yuanmeng.zwolfserver.server.manager;


import java.nio.charset.Charset;
import java.util.Map;

import org.yuanmeng.zwolfserver.metadata.business.Packet;
import org.yuanmeng.zwolfserver.server.im.IMChannelGroup;
import org.yuanmeng.zwolfserver.metadata.business.ProtocolField;
import org.yuanmeng.zwolfserver.metadata.business.UserChannel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * 用户信息转发器
 * author: vector.huang
 * date：2016/4/18 22:37
 */
public class IMUserManager {

    /**
     * 登录，返回userId，响应userId
     *
     * @param channel
     * @param body
     * @return
     */
    public static int loginReq(Channel channel, ByteBuf body) {

        String username = body.readBytes(body.readInt()).toString(Charset.defaultCharset());
        String password = body.readBytes(body.readInt()).toString(Charset.defaultCharset());

        //保存Channel 和 用户信息到IMChannelGroup
        UserChannel userChannel = new UserChannel();
        userChannel.setChannel(channel);
        userChannel.setUserName(username);
        int userId = IMChannelGroup.instance().put(userChannel);
        userChannel.setUserId(userId);

        //返回userId
        ByteBuf byteBuf = channel.alloc().buffer();
        byteBuf.writeInt(userId);
        Packet packet = new Packet(byteBuf.readableBytes() + 12, ProtocolField.SID_USER,
                ProtocolField.CID_USER_LOGIN_RESP, byteBuf);
        channel.writeAndFlush(packet);

        System.out.println("连接-在线用户为: " + IMChannelGroup.instance().size());
        return userId;
    }

    /**
     * 登出，断开连接
     *
     * @param userId
     */
    public static void logout(int userId) {
        IMChannelGroup.instance().remove(userId);
        System.out.println("断开-在线用户为: " + IMChannelGroup.instance().size());
    }

    public static void onlineUserReq(Channel channel, ByteBuf body) {

        ByteBuf users = channel.alloc().buffer();
        Map<Integer, UserChannel> userChannels = IMChannelGroup.instance().getChannels();

        //在线总人数
        users.writeInt(userChannels.size());
        //用户列表
        userChannels.forEach((userId, userChannel) -> {
            byte[] bytes = userChannel.getUserName().getBytes();
            users.writeInt(userId)
                    .writeInt(bytes.length)
                    .writeBytes(bytes);
        });

        Packet packet = new Packet(users.readableBytes() + 12, ProtocolField.SID_USER
                , ProtocolField.CID_USER_ONLINE_RESP, users);
        channel.writeAndFlush(packet);

    }

}
