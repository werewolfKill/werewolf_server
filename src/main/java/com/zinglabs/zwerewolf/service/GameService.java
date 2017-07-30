package com.zinglabs.zwerewolf.service;

import com.zinglabs.zwerewolf.constant.GlobalData;
import com.zinglabs.zwerewolf.entity.Room;
import com.zinglabs.zwerewolf.entity.role.UserRole;
import com.zinglabs.zwerewolf.util.RoomUtil;

import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author wangtonghe
 * @date 2017/7/26 00:09
 */
public class GameService {

    private GlobalData globalData = GlobalData.getInstance();


    /**
     * 检查并获取房间
     * @param roomId 房间号
     * @param owner 房主
     * @return room
     */
    public Room checkRoom(int roomId,int owner){
        Room room  = globalData.getRoomData().get(roomId);
        if(room==null||room.getOwner()!=owner){
            return null;
        }
        return room;
    }


    /**
     * 分配角色
     * @param room 房间
     * @return 角色列表
     */
    public Map<Integer,UserRole> allotRoles(Room room){
        Map<Integer,UserRole> userRoleMap = room.getPlayers();
        int modalId = room.getModalId();
        List<Integer> roleList = RoomUtil.getRolesByModalId(modalId);

        userRoleMap.forEach((userId,ur)->{
            int rom_roleId = new Random().nextInt(roleList.size());
            ur.setRoleId(roleList.get(rom_roleId));
            roleList.remove(rom_roleId);
        });
        return userRoleMap;

    }

    /**
     * 准备游戏
     * @param userId 玩家id
     * @param roomId 房间id
     */
    public void readyGame(int userId,int roomId){





    }


}
