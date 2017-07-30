package com.zinglabs.zwerewolf.service;

import com.zinglabs.zwerewolf.constant.GlobalData;
import com.zinglabs.zwerewolf.entity.Room;
import com.zinglabs.zwerewolf.entity.UserChannel;
import com.zinglabs.zwerewolf.entity.role.UserRole;
import com.zinglabs.zwerewolf.im.IMChannelGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wangtonghe
 * @date 2017/7/30 19:03
 */
public class UserService {

    private GlobalData globalData = GlobalData.getInstance();

    /**
     * 根据房间id获取用户
     *
     * @param roomId 房间id
     * @return 该房间用户channel集合
     */
    public Map<Integer, UserChannel> getByRoomId(int roomId) {
        Room room = globalData.getRoomData().get(roomId);
        Map<Integer, UserRole> players = room.getPlayers();
        Map<Integer, UserChannel> allUserChan = IMChannelGroup.instance().getChannels();
        Map<Integer, UserChannel> retUserChan = new HashMap<>();
        players.forEach((id, ur) -> {
            retUserChan.put(id, allUserChan.get(id));
        });
        return retUserChan;
    }

    /**
     * 获取指定房间某一用户
     *
     * @param roomId 房间id
     * @param userId 用户id
     * @return 该用户channel
     */
    public UserChannel getByUserId(int roomId, int userId) {
        Room room = globalData.getRoomData().get(roomId);
        Map<Integer, UserRole> players = room.getPlayers();
        UserChannel retUserChan = new UserChannel();
        if (players.containsKey(userId)) {
            Map<Integer, UserChannel> allUserChan = IMChannelGroup.instance().getChannels();
            retUserChan = allUserChan.get(userId);
        }
        return retUserChan;
    }

    /**
     * 根据userId获取用户channel
     *
     * @param userId 用户id
     * @return 该用户channel
     */
    public UserChannel getByUserId(int userId) {
        Map<Integer, UserChannel> allUserChan = IMChannelGroup.instance().getChannels();
        UserChannel retUserChan = allUserChan.get(userId);
        return retUserChan;
    }


}
