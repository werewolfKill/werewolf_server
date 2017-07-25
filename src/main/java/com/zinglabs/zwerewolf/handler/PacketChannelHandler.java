package com.zinglabs.zwerewolf.handler;

import com.zinglabs.zwerewolf.config.ProcessorTable;
import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.entity.Packet;
import com.zinglabs.zwerewolf.im.ThreadServerSocket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 代表一个Channel连接，保存着
 *
 * author: vector.huang date：2016/4/18 19:25
 */
public class PacketChannelHandler extends ChannelInboundHandlerAdapter {
	private static final ByteBuf Channel = null;
	private ThreadServerSocket.OnChannelActiveListener listener;
	private Map<String,Object> application = new HashMap<>();
	public PacketChannelHandler(ThreadServerSocket.OnChannelActiveListener listener) {
		this.listener = listener;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
		if (listener != null) {
            listener.onChannelActive(ctx);
        }
	} @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ProcessorTable.getAll().forEach((key,instance)->{
        	instance.doDestory(application);
        });
    }

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Packet packet = (Packet) msg;
		BaseController action = ProcessorTable.get(packet.getServiceId());
        if(action != null){
            action.doAccept(packet.getCommandId(),ctx.channel(),packet.getBody(),application);
        }
	}
}
