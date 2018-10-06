package org.yuanmeng.zwolfserver.data;

import org.yuanmeng.zwolfserver.entity.Room;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangtonghe
 * @since 2018/10/6 14:18
 */
public class RoomChannelTable {

    private static Map<Integer, Room> table = new ConcurrentHashMap<>();

    public static void addRoom(int roomId, Room room) {
        table.putIfAbsent(roomId, room);
    }

    public static void removeUser(int roomId) {
        table.remove(roomId);
    }

    public static Room getRoom(int roomId) {
        return table.get(roomId);
    }

    public static int getTotal() {
        return table.size();
    }
}
