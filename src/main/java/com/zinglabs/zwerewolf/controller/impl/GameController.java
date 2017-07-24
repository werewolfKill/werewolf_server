package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.manager.IMMessageManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.Map;

/**
 * 游戏流程控制器
 * @author wangtonghe
 * @date 2017/7/24 08:49
 */
public class GameController implements BaseController {
    @Override
    public void doDestory(Map application) {

    }

    @Override
    public void doAccept(short commandId, Channel channel, ByteBuf body, Map application) {
        switch (commandId) {
            case ProtocolConstant.CID_GAME_READY_REQ:
                IMMessageManager.sendGroupTextReq(body);
                break;
            case ProtocolConstant.CID_MSG_VOICE_REQ:
                IMMessageManager.sendGroupVoiceReq(body);
                break;
        }

    }
}
