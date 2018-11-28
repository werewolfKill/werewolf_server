package org.yuanmeng.zwolfserver.service;

import org.springframework.stereotype.Service;
import org.yuanmeng.zwolfserver.data.RoomChannelTable;
import org.yuanmeng.zwolfserver.metadata.business.Room;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangtonghe
 * @date 2017/7/26 09:31
 */
@Service
public class BusinessService {


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
        // 房主进入房间
        room.enterRoom(userId);
        RoomChannelTable.addRoom(room_Id, room);
        return room;
    }


    /**
     * 搜索房间并进入
     *
     * @param roomId 房间号
     * @return room
     */
    public Room searchRoom(int roomId) {

        return RoomChannelTable.getRoom(roomId);

    }
}










