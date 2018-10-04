package org.yuanmeng.zwerewolf.server.controller.impl;

import java.util.Map;

import org.yuanmeng.zwerewolf.server.constant.ProtocolConstant;
import org.yuanmeng.zwerewolf.server.controller.BaseController;
import org.yuanmeng.zwerewolf.server.manager.IMUserManager;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public class UserController implements BaseController{

	@Override
	public void doDestory(Map application) {
		IMUserManager.logout((Integer)application.get("currentUserId"));
	}

	@Override
	public void doAccept(short commandId, Channel channel, ByteBuf body,Map<String,Object> application) {
		switch (commandId) {
		case ProtocolConstant.CID_USER_LOGIN_REQ:
			int userId = IMUserManager.loginReq(channel, body);
			application.put("currentUserId", userId);
			break;
		case ProtocolConstant.CID_USER_ONLINE_REQ:
			IMUserManager.onlineUserReq(channel, body);
			break;
		}
	}

}
