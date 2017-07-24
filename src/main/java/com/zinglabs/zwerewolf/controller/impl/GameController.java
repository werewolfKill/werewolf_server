package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.entity.BNSMsgBody;
import com.zinglabs.zwerewolf.entity.ByteBufMsg;
import com.zinglabs.zwerewolf.entity.UserChannel;
import com.zinglabs.zwerewolf.im.IMChannelGroup;
import com.zinglabs.zwerewolf.manager.IMBusinessManager;
import com.zinglabs.zwerewolf.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.Map;

/**
 * 游戏流程控制器
 *
 * @author wangtonghe
 * @date 2017/7/24 08:49
 */
public class GameController implements BaseController {
    @Override
    public void doDestory(Map application) {

    }

    @Override
    public void doAccept(short commandId, Channel channel, ByteBuf body, Map application) {
        ByteBufMsg byteBufMsg = ByteBufUtil.encodeBNS(body);
        int userId = byteBufMsg.getFromId();
        int roomId = byteBufMsg.getRoomId();
        BNSMsgBody bnsMsgBody = null;

        switch (commandId) {
            case ProtocolConstant.CID_GAME_READY_REQ:  //准备

                bnsMsgBody = new BNSMsgBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_READY_REQ,
                        userId, roomId, userId);
                //模拟所有玩家用户  //TODO 根据房间号搜索用户
                Map<Integer, UserChannel> readyChannels = IMChannelGroup.instance().getChannels();

                IMBusinessManager.sendGroup(bnsMsgBody, readyChannels);

                //TODO 添加判断是否所有玩家准备好，则直接进入游戏（天黑）
                if (readyChannels.size() == 12) {
                    bnsMsgBody.setCommonId(ProtocolConstant.CID_GAME_DARK);
                    bnsMsgBody.setReply(0);
                    //发送天黑命令，客户端各角色进入自己状态
                    IMBusinessManager.sendGroup(bnsMsgBody, readyChannels);

                }
                break;
            case ProtocolConstant.CID_GAME_KILL_REQ:   //狼人杀人

                bnsMsgBody = new BNSMsgBody(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_READY_REQ,
                        userId, roomId, userId);
                //模拟所有玩家用户  //TODO 根据房间号搜索用户
                Map<Integer, UserChannel> userChannels = IMChannelGroup.instance().getChannels();

                IMBusinessManager.sendGroup(bnsMsgBody, userChannels);

                break;
        }

    }
}
