package com.zinglabs.zwerewolf.manager;

import com.zinglabs.zwerewolf.entity.UserChannel;
import io.netty.buffer.ByteBuf;

import java.util.Map;

/**
 * 业务消息转发器
 * @author wangtonghe
 * @date 2017/7/24 08:58
 */
public class IMBusinessManager {

    /**
     * 通用转发业务消息
     * @param body 消息体
     * @param senders 发送者集合
     */
    public static void sendGroup(ByteBuf body, Map<Integer,UserChannel> senders){


    }
}
