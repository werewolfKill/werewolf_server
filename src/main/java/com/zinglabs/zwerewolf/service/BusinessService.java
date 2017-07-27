package com.zinglabs.zwerewolf.service;

import java.util.Random;

/**
 * @author wangtonghe
 * @date 2017/7/26 09:31
 */
public class BusinessService {


    /**
     * 创建房间
     * @param userId 用户id
     * @param modelId 模式id
     * @return 房间号
     */
    public int createRoom(int userId,int modelId){
        //TODO 创建房间逻辑
        return new Random().nextInt(10000);
    }
}
