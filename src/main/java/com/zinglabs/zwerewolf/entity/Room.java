package com.zinglabs.zwerewolf.entity;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 房间实体类
 * @author wangtonghe
 * @date 2017/7/24 18:20
 */
public class Room {

    /**
     * 房间id
     */
    private int id;

    /**
     * 房间人数
     */
    private int number;

    /**
     * 号数与用户id对应Map
     * 如1号玩家对应id为1001的用户，则 1:1001
     */
    private Map<Integer,Integer> people = new TreeMap<>();








}
