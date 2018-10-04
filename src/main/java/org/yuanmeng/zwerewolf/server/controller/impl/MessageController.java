package org.yuanmeng.zwerewolf.server.controller.impl;

import java.util.Map;

import org.yuanmeng.zwerewolf.server.controller.BaseController;
import org.yuanmeng.zwerewolf.server.constant.ProtocolConstant;
import org.yuanmeng.zwerewolf.server.manager.IMMessageManager;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public class MessageController implements BaseController {

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
