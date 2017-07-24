package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.controller.BaseController;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.Map;

/**
 * @author wangtonghe
 * @date 2017/7/24 12:24
 */
public class BusinessController implements BaseController{
    @Override
    public void doDestory(Map application) {

    }

    @Override
    public void doAccept(short commandId, Channel channel, ByteBuf body, Map application) {

    }
}
