package org.yuanmeng.zwerewolf.server.controller;

import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public interface BaseController {
    void doDestory(Map application);

	void doAccept(short commandId,Channel channel, ByteBuf body,Map<String,Object> application);
}