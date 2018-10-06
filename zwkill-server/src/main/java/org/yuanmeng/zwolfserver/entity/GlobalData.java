package org.yuanmeng.zwolfserver.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据暂存区
 *
 * @author wangtonghe
 * @date 2017/7/29 11:24
 */
public class GlobalData {

    private volatile static GlobalData instance;

    private Map<Integer, Room> roomData = new HashMap<>();

    private Map<Integer, GameInfo> gameData = new HashMap<>();

    public static GlobalData getInstance() {
        if (instance == null) {
            synchronized (GlobalData.class) {
                if (instance == null) {
                    instance = new GlobalData();
                }
            }
        }
        return instance;
    }

    public Map<Integer, Room> getRoomData() {
        return roomData;
    }

    public void putRoomData(Integer integer, Room room) {

        roomData.put(integer, room);
    }

    public Map<Integer, GameInfo> getGameData() {
        return gameData;
    }

    public void putGameData(int roomId, GameInfo gameInfo) {
        this.gameData.put(roomId, gameInfo);
    }
}
