package org.yuanmeng.zwolfserver.controller;

import io.netty.channel.Channel;
import org.springframework.stereotype.Controller;
import org.yuanmeng.zwolfserver.annotation.RequestMapping;
import org.yuanmeng.zwolfserver.entity.RequestInfo;
import org.yuanmeng.zwolfserver.entity.ResponseBody;
import org.yuanmeng.zwolfserver.entity.Room;
import org.yuanmeng.zwolfserver.entity.role.UserRole;
import org.yuanmeng.zwolfserver.service.BusinessService;

import java.util.HashMap;
import java.util.Map;

/**
 * 选择房间、模式等业务操作
 *
 * @author wangtonghe
 * @since 2018/10/5 21:04
 */
@Controller
public class RoomController {

    private BusinessService businessService = new BusinessService();

    /**
     * 创建房间
     */
    @RequestMapping(value = "/room/create")
    public void createRoom(Integer userId, Integer modelId, RequestInfo requestInfo) {
        Room curRoom = businessService.createRoom(userId, modelId);
        String url = requestInfo.getTargetUrl();
        Channel channel = requestInfo.getChannel();
        ResponseBody responseBody = new ResponseBody();
        responseBody.setUrl(url);
        responseBody.setCode(0);
        Map<String, Object> body = new HashMap<>(2);
        body.put("roomId", curRoom.getId());
        responseBody.setParam(body);
        channel.writeAndFlush(responseBody);
    }

    @RequestMapping(value = "/room/search")
    public void searchRoom(int userId, int roomId, RequestInfo requestInfo) {
        Channel channel = requestInfo.getChannel();
        String targetUrl = requestInfo.getTargetUrl();
        Room curRoom = businessService.searchRoom(roomId);
        ResponseBody responseBody = new ResponseBody();

        //房间不存在
        if (curRoom == null) {
            responseBody.setUrl(targetUrl);
            responseBody.setCode(-1);
            channel.writeAndFlush(responseBody);
        }
        //房间进入成功,通知房间所有人
        else if (curRoom.enterRoom(userId)) {
            Map<Integer, UserRole> userRoleMap = curRoom.getPlayers();
            int position = userRoleMap.get(userId).getPosition();
            Map<String, Object> param = new HashMap<>(2);
            param.put("position", position);
            param.put("roomId", roomId);
            ResponseBody notifyResponse = new ResponseBody(0, "/room/enter", param);
            ResponseBody selfResponse = new ResponseBody(0, targetUrl, null);

            userRoleMap.forEach((id, role) -> {
                //对于自己，进入房间
                if (id == userId) {
                    channel.writeAndFlush(selfResponse);
                }
                //对于其他人，通知有人进入房间
                else {
                    channel.writeAndFlush(notifyResponse);
                }
            });
        }
        //房间已满
        else {
            responseBody = new ResponseBody(-2, targetUrl, null);
            channel.writeAndFlush(responseBody);
        }
    }


}
