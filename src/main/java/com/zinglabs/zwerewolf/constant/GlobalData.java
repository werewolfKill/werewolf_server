package com.zinglabs.zwerewolf.constant;

import com.zinglabs.zwerewolf.entity.Room;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据暂存区
 * @author wangtonghe
 * @date 2017/7/29 11:24
 */
public class GlobalData {

    private volatile static  GlobalData instance;

    private  Map<Integer,Room> roomData = new HashMap<>();

    public static GlobalData getInstance(){
        if(instance==null){
            synchronized (GlobalData.class){
                if(instance==null){
                    instance =new GlobalData();
                }
            }
        }
        return instance;
    }

    public Map<Integer, Room> getRoomData() {
        return roomData;
    }

    public void putRoomData(Integer integer,Room room){

        roomData.put(integer,room);
    }


}
