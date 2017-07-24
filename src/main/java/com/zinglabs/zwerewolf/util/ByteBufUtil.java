package com.zinglabs.zwerewolf.util;

import com.zinglabs.zwerewolf.entity.MessageBody;
import io.netty.buffer.ByteBuf;

/**
 * 消息体工具类
 * @author wangtonghe
 * @date 2017/7/24 09:20
 */
public class ByteBufUtil {

    /**
     * 解析消息体
     * @param body 消息体
     * @return MessageBody
     */
    public static final MessageBody encode(ByteBuf body){
        int fromId = body.readInt();
        int length = body.readInt();
        byte[] contentByte = new byte[length];
        body.readBytes(contentByte);
        MessageBody msgBody = new MessageBody();
        msgBody.setContent(contentByte);
        msgBody.setFromId(fromId);
        msgBody.setLength(length);
        return msgBody;
    }


}
