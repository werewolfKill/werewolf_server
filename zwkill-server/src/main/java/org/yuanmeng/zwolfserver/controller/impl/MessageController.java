package org.yuanmeng.zwolfserver.controller.impl;

import java.util.Map;

import org.yuanmeng.zwolfserver.controller.BaseController;
import org.yuanmeng.zwolfserver.entity.ProtocolField;
import org.yuanmeng.zwolfserver.server.manager.IMMessageManager;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public class MessageController implements BaseController {

	@Override
	public void doDestory(Map application) {
		
	}

	@Override
	public void doAccept(short commandId, Channel channel, ByteBuf body,Map<String,Object> application) {
		
			switch (commandId) {
			case ProtocolField.CID_MSG_TEXT_REQ:
				IMMessageManager.sendGroupTextReq(body);
				break;
			case ProtocolField.CID_MSG_VOICE_REQ:
				IMMessageManager.sendGroupVoiceReq(body);
				break;	
			}
			
	}

	

}
