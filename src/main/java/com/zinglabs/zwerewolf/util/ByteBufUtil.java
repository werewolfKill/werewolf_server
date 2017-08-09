package com.zinglabs.zwerewolf.util;

import com.zinglabs.zwerewolf.entity.RequestBody;
import io.netty.buffer.ByteBuf;

/**
 * 消息体工具类
 *
 * @author wangtonghe
 * @date 2017/7/24 09:20
 */
public class ByteBufUtil {

    /**
     * 解析游戏类型消息体
     *
     * @param body 消息体
     * @return RequestBody
     */
    public static RequestBody resolveGame(ByteBuf body) {
        int fromId = body.readInt();
        int roomId = body.readInt();
        int bout = body.readInt();
        int order = body.readInt();
        RequestBody msgBody = new RequestBody();
        msgBody.setCode(order);
        msgBody.setFromId(fromId);
        msgBody.setRoomId(roomId);
        msgBody.setBout(bout);
        return msgBody;
    }

    /**
     * 解析业务相关消息体
     * @param body 消息体
     * @return RequestBody
     */
    public static RequestBody resolveBusiness(ByteBuf body){
        int fromId = body.readInt();
        int order = body.readInt();
        RequestBody msgBody = new RequestBody();
        msgBody.setCode(order);
        msgBody.setFromId(fromId);
        return msgBody;

    }




}
