package com.zinglabs.zwerewolf.manager;

import com.zinglabs.zwerewolf.entity.ResponseBody;
import com.zinglabs.zwerewolf.entity.Packet;
import com.zinglabs.zwerewolf.entity.Room;
import com.zinglabs.zwerewolf.entity.UserChannel;
import com.zinglabs.zwerewolf.entity.role.UserRole;
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
     * 转发业务消息，通用方法
     *
     * @param senders 发送者集合
     */
    public static void sendGroup(Map<ResponseBody, UserChannel> senders) {

        senders.forEach((body, userChannel) -> {
            if(userChannel==null){
                return;
            }
            Channel toChannel = userChannel.getChannel();
            if(toChannel==null){
                return;
            }
            ByteBuf byteBuf = toChannel.alloc().buffer(8);  //2个int
            byteBuf.writeInt(body.getFromId());
            byteBuf.writeInt(body.getReply());

            Packet packet = new Packet(12 + byteBuf.readableBytes(), body.getServiceId(), body.getCommand(), byteBuf);
            userChannel.getChannel().writeAndFlush(packet);
        });

    }

    /**
     * 转发房间信息
     *
     * @param senders 发送者及发送消息集合
     */
    public static void sendRoomMsg(Map<ResponseBody, UserChannel> senders) {
        senders.forEach((body, userChannel) -> {
            if(userChannel==null){
                return;
            }
            Channel toChannel = userChannel.getChannel();
            if(toChannel==null){
                return;
            }
            ByteBuf byteBuf = toChannel.alloc().buffer();
            byteBuf.writeInt(body.getFromId());
            byteBuf.writeInt(body.getReply());
            Map param = body.getParam();
            if (param != null && param.get("room") != null) {
                Room room = (Room) param.get("room");
                byteBuf.writeInt(1);  //标识是否结束
                byteBuf.writeInt(room.getId());  //房间id
                byteBuf.writeInt(room.getOwner());
                byteBuf.writeInt(room.getModalId());
                byteBuf.writeInt(room.getCurNumber());  //当前人数
                Map<Integer, UserRole> players = room.getPlayers();
                if (players != null && players.size() > 0) {
                    players.forEach((userId, role) -> {
                        byteBuf.writeInt(userId);
                        byteBuf.writeInt(role.getPosition());
                    });
                }
            } else {
                byteBuf.writeInt(0); //标识是否结束
            }
            Packet packet = new Packet(12 + byteBuf.readableBytes(), body.getServiceId(), body.getCommand(), byteBuf);
            userChannel.getChannel().writeAndFlush(packet);

        });

    }

    /**
     * 转发开场信息
     *
     * @param senders 发送者集合
     */
    public static void sendStartMsg(Map<ResponseBody, UserChannel> senders) {
        senders.forEach((body, userChan) -> {
            if(userChan==null){
                return;
            }
            Channel toChannel = userChan.getChannel();
            if(toChannel==null){
                return;
            }
            ByteBuf byteBuf = toChannel.alloc().buffer();
            byteBuf.writeInt(body.getFromId());
            byteBuf.writeInt(body.getReply());
            Map param = body.getParam();
            if (param != null && param.get("wolfs") != null) {
                List<Integer> wolfs = (List<Integer>) param.get("wolfs");
                byteBuf.writeInt(wolfs.size());
                wolfs.forEach(byteBuf::writeInt);
            } else {
                byteBuf.writeInt(0); //标识是否结束
            }
            Packet packet = new Packet(12 + byteBuf.readableBytes(), body.getServiceId(), body.getCommand(), byteBuf);
            toChannel.writeAndFlush(packet);

        });
    }

    /**
     * 转发天亮信息
     *
     * @param senders 发送者集合
     */
    public static void sendDawnMsg(Map<ResponseBody, UserChannel> senders) {
        senders.forEach((body, userChan) -> {
            if(userChan==null){
                return;
            }
            Channel toChannel = userChan.getChannel();
            if(toChannel==null){
                return;
            }
            ByteBuf byteBuf = toChannel.alloc().buffer();
            byteBuf.writeInt(body.getFromId());
            byteBuf.writeInt(body.getReply());
            Map<String,Object> param = body.getParam();
            if(param!=null&&param.get("killed")!=null){
                List<Integer> killed = (List<Integer>) param.get("killed");
                byteBuf.writeInt(killed.size());
                killed.forEach(byteBuf::writeInt);
            }
            Packet packet = new Packet(12 + byteBuf.readableBytes(), body.getServiceId(), body.getCommand(), byteBuf);
            toChannel.writeAndFlush(packet);

        });
    }


}
