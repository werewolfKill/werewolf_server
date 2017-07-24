package com.zinglabs.zwerewolf.util;

import com.zinglabs.zwerewolf.entity.business.BNSRequest;
import io.netty.buffer.ByteBuf;

/**
 * 消息体工具类
 *
 * @author wangtonghe
 * @date 2017/7/24 09:20
 */
public class ByteBufUtil {

    /**
     * 解析业务类型消息体(包括业务及游戏流程服务类型)
     *
     * @param body 消息体
     * @return BNSRequest
     */
    public static BNSRequest encodeBNS(ByteBuf body) {
        int fromId = body.readInt();
        int roomId = body.readInt();
        int order = body.readInt();
        BNSRequest msgBody = new BNSRequest();
        msgBody.setContent(order);
        msgBody.setFromId(fromId);
        msgBody.setRoomId(roomId);
        return msgBody;
    }


}
