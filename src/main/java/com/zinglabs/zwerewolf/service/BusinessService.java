package com.zinglabs.zwerewolf.service;

import com.zinglabs.zwerewolf.config.Config;
import com.zinglabs.zwerewolf.constant.GlobalData;
import com.zinglabs.zwerewolf.entity.Room;
import com.zinglabs.zwerewolf.entity.UserChannel;
import com.zinglabs.zwerewolf.entity.role.UserRole;
import com.zinglabs.zwerewolf.im.IMChannelGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangtonghe
 * @date 2017/7/26 09:31
 */
public class BusinessService {

    private GlobalData globalData = GlobalData.getInstance();

    private static int room_Id = 10000;

    private Lock lock = new ReentrantLock();


    /**
     * 创建房间
     *
     * @param userId  用户id
     * @param modelId 模式id
     * @return 房间号
     */
    public Room createRoom(int userId, int modelId) {
        lock.lock();
        try {
            room_Id++;
        } finally {
            lock.unlock();
        }
        Room room = new Room(room_Id, modelId, userId);
        room.enterRoom(userId);
        //----模拟数据测试用 开始------
        int start = room.getCurNumber()+1;
        for(int i=start;i<room.getNumber();i++){
            int tid = 200+i;
            IMChannelGroup.instance().setUserChannel(tid,new UserChannel());
            room.enterRoom(tid);
        }
        //----模拟数据测试用 结束------
        globalData.putRoomData(room_Id, room);
        return room;
    }


    /**
     * 搜索房间并进入
     *
     * @param userId 用户id
     * @param roomId 房间号
     * @return 0表示进入成功，-1表示没有此房间，-2表示此房间已满
     */
    public Room searchAndEnterRoom(int userId, int roomId) {

        return  globalData.getRoomData().get(roomId);

    }
}










