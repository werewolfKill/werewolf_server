package com.zinglabs.zwerewolf.entity;

import java.util.*;

/**
 * 游戏信息
 *
 * @author wangtonghe
 * @date 2017/8/2 08:08
 */
public class GameInfo {

    private Map<Integer, Integer> killInfo = new HashMap<>();





    /**
     * 添加杀人信息
     * @param userId 用户id
     */
    public void putKillInfo(int userId) {
        Integer num = killInfo.putIfAbsent(userId, 1);
        if(num!=null){
            killInfo.put(userId,num+1);
        }
    }

    /**
     * 获取目前几个狼人已发送杀人请求
     */
    public int getKillNumber(){
        return killInfo.size();

    }


    /**
     * 返回狼人杀人信息
     *
     * @return 0为意见持平，其他为玩家id
     */
    public int getKilled(){
        List<Map.Entry<Integer,Integer>> killList = new ArrayList<>(killInfo.entrySet());
        Collections.sort(killList,(o1,o2)->o2.getValue().compareTo(o1.getValue()));
        int first = killList.get(0).getValue();
        int second = killList.get(1).getValue();
        if(first==second){
            return 0;
        }else{
            return killList.get(0).getKey();
        }
    }

}
