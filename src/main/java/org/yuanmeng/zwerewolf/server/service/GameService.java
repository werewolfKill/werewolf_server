package org.yuanmeng.zwerewolf.server.service;

import org.yuanmeng.zwerewolf.server.entity.GameInfo;
import org.yuanmeng.zwerewolf.server.util.GameUtil;
import org.yuanmeng.zwerewolf.server.constant.GlobalData;
import org.yuanmeng.zwerewolf.server.entity.Room;
import org.yuanmeng.zwerewolf.server.entity.role.UserRole;

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
     *
     * @param roomId 房间号
     * @param owner  房主
     * @return room
     */
    public Room checkAndGetRoom(int roomId, int owner) {

        Room room = globalData.getRoomData().get(roomId);
        if (room == null || room.getOwner() != owner) {
            return null;
        }
        return room;
    }
    /**
     * 检查并获取房间
     *
     * @param roomId 房间号
     * @return room
     */
    public Room checkAndGetRoom(int roomId) {

        Room room = globalData.getRoomData().get(roomId);
        if (room == null ) {
            return null;
        }
        return room;
    }

    public GameInfo getGameInfo(int roomId){
       return globalData.getGameData().get(roomId);
    }




    /**
     * 分配角色
     *
     * @param room 房间
     * @return 角色列表
     */
    public Map<Integer, UserRole> allotRoles(Room room) {
        Map<Integer, UserRole> userRoleMap = room.getPlayers();
        int modalId = room.getModalId();
        List<Integer> roleList = GameUtil.getRolesByModalId(modalId);

        userRoleMap.forEach((userId, ur) -> {
            int rom_roleId = new Random().nextInt(roleList.size());
            ur.setRoleId(roleList.get(rom_roleId));
            ur.setRole(GameUtil.getRole(ur.getRoleId()));

            //测试
//            if(ur.getPosition()==1){
//                ur.setRoleId(Config.ROLE_CODE_OF_WOLF);
//                ur.setRole(GameUtil.getRole(ur.getRoleId()));
//            }
//            if(ur.getPosition()==11){
//                ur.setRoleId(Config.ROLE_CODE_OF_WITCH);
//                ur.setRole(GameUtil.getRole(ur.getRoleId()));
//            }
//            if(ur.getPosition()==12){
//                ur.setRoleId(Config.ROLE_CODE_OF_GUARD);
//                ur.setRole(GameUtil.getRole(ur.getRoleId()));
//            }
            roleList.remove(rom_roleId);
        });

        //测试
//        userRoleMap.forEach((id,ur)->{
//            if(ur.getPosition()!=11&&ur.getRoleId()==Config.ROLE_CODE_OF_WITCH){
//                ur.setRoleId(Config.ROLE_CODE_OF_VILLAGER);
//            }
//        });

//        userRoleMap.put()
        return userRoleMap;

    }


    /**
     * 准备/离开游戏
     * @param userId 玩家id
     * @param roomId 房间id
     * @return 准备状态
     */
    public int readyGame(int userId, int roomId) {
        Room room = globalData.getRoomData().get(roomId);
        if (room == null) {
            return 0;
        }
        int  ready = 0;
        UserRole userRole = room.getPlayers().get(userId);
        if(userRole!=null){
            if(userRole.isReady()){
                userRole.setReady(false);
                ready = 0;
            }else{
                userRole.setReady(true);
                ready = 1;
            }
        }
        return ready;
    }

    public boolean isAllReady(Room room) {
        Map<Integer, UserRole> players = room.getPlayers();
        int owner = room.getOwner();
        boolean isReady = true;
        for (UserRole ur : players.values()) {
            if (!ur.isReady() && ur.getUserId() != owner) {
                isReady = false;
                break;
            }
        }
        return isReady;
    }
}
