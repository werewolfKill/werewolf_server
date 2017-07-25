package com.zinglabs.zwerewolf.controller.impl;

import com.zinglabs.zwerewolf.constant.ProtocolConstant;
import com.zinglabs.zwerewolf.controller.BaseController;
import com.zinglabs.zwerewolf.entity.business.BNSRequest;
import com.zinglabs.zwerewolf.entity.business.BNSResponse;
import com.zinglabs.zwerewolf.entity.UserChannel;
import com.zinglabs.zwerewolf.im.IMChannelGroup;
import com.zinglabs.zwerewolf.manager.IMBusinessManager;
import com.zinglabs.zwerewolf.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.HashMap;
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
    public void doAccept(short commandId, Channel channel, ByteBuf body, Map<String,Object> application) {
        BNSRequest bnsRequest = ByteBufUtil.encodeBNS(body);
        int fromId = bnsRequest.getFromId();
        int roomId = bnsRequest.getRoomId();
        int content = (Integer) bnsRequest.getContent();
        BNSResponse bnsResponse = null;

        switch (commandId) {
            case ProtocolConstant.CID_GAME_READY_REQ:  //准备游戏

                bnsResponse = new BNSResponse(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_READY_RESP,
                        fromId, roomId, 0);
                //模拟所有玩家用户  //TODO 根据房间号搜索用户
                Map<Integer, UserChannel> readyChannels = IMChannelGroup.instance().getChannels();

                IMBusinessManager.sendGroup(bnsResponse, readyChannels);

                //TODO 添加判断是否所有玩家准备好，则直接进入游戏（天黑）
                if (readyChannels.size() == 1) {
                    bnsResponse.setCommand(ProtocolConstant.CID_GAME_DARK);
                    bnsResponse.setReply(0);

                    //发送天黑命令，客户端各角色进入自己状态
                    IMBusinessManager.sendGroup(bnsResponse, readyChannels);

                }
                break;
            case ProtocolConstant.CID_GAME_KILL_REQ:   //狼人杀人
                //向所有狼人发送杀人信息
                bnsResponse = new BNSResponse(ProtocolConstant.SID_GAME, ProtocolConstant.CID_GAME_KILL_RESP,
                        fromId, roomId, content);
                //模拟所有玩家用户  //TODO 根据房间号搜索狼人
                Map<Integer,UserChannel>  wolfs = new HashMap<>();
                Map<Integer, UserChannel> userChannels = IMChannelGroup.instance().getChannels();
                userChannels.forEach((userId,chan)->{
                    if(userId%2==1){
                        wolfs.put(userId,chan);
                    }
                });
                IMBusinessManager.sendGroup(bnsResponse, wolfs);
                break;
        }

    }
}
