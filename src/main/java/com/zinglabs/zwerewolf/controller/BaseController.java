package com.zinglabs.zwerewolf.controller;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public interface BaseController {
    void doAccept(short command, ByteBuf body);

    void doSend(short command,Channel channel,Object param);
}