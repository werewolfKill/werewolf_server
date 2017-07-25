package com.zinglabs.zwerewolf.controller.impl;

import java.util.Map;

import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.manager.IMMessageManager;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public class MessageController implements BaseController{

	@Override
	public void doDestory(Map application) {
		
	}

	@Override
	public void doAccept(short commandId, Channel channel, ByteBuf body,Map<String,Object> application) {
		
			switch (commandId) {
			case ProtocolConstant.CID_MSG_TEXT_REQ:
				IMMessageManager.sendGroupTextReq(body);
				break;
			case ProtocolConstant.CID_MSG_VOICE_REQ:
				IMMessageManager.sendGroupVoiceReq(body);
				break;	
			}
			
	}

	

}
