package com.zinglabs.zwerewolf.manager;

import java.util.Map;

import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.entity.Packet;
import com.zinglabs.zwerewolf.entity.UserChannel;
import com.zinglabs.zwerewolf.im.IMChannelGroup;

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
    public static void sendGroupMsgReq(int to,ByteBuf body){
        int fromId = body.readInt();
        byte[] contentByte = new byte[body.readableBytes()];  
        //4、复制内容到字节数组b  
        body.readBytes(contentByte);  
        Map<Integer, UserChannel> userChannels = IMChannelGroup.instance().getChannels();
        userChannels.forEach((userId,userChannel)->{
        	Channel toChannel = userChannel.getChannel();
        	
        	byte[] fromName = userChannels.get(fromId).getUsername().getBytes();
        	
        	ByteBuf msg = toChannel.alloc().buffer(contentByte.length + fromName.length + 12);
        	
            msg.writeInt(fromId);
            
            msg.writeInt(fromName.length);
            msg.writeBytes(fromName);
            
            msg.writeInt(contentByte.length);
            msg.writeBytes(contentByte);
            Packet packet = new Packet(msg.readableBytes()+12, ProtocolConstant.SID_MSG,ProtocolConstant.CID_MSG_RECEIVE_SINGLE_OUT,
                    msg);
            toChannel.writeAndFlush(packet);
        });
    }

}
