package com.zinglabs.zwerewolf.config;

/**
 * author: vector.huang
 * date：2016/4/20 20:42
 */
public interface Config {

    String SERVER_HOST = "192.168.0.102";
    int SERVER_PORT = 8080;
    int LOGIN_PORT = 8090;

    //游戏模式
    int MODEL_12_YNLS = 1;  //12人预女列守经典版
    int MODEL_12_YNLB = 2;  //12人预女列白版
    int MODEL_9 = 3;  //9人局
    int MODEL_10 = 4;  //10人局

    //角色对应关系
    int ROLE_CODE_OF_WOLF = 1;  //狼
    int ROLE_CODE_OF_PROPHET = 2;  //预言家
    int ROLE_CODE_OF_VILLAGER = 3;  //村民
    int ROLE_CODE_OF_WITCH = 4;  //女巫
    int ROLE_CODE_OF_HUNTSMAN = 5;  //猎人
    int ROLE_CODE_OF_GUARD = 6;  //守卫
    int ROLE_CODE_OF_IDIOT = 7;  //白痴

//    游戏过程状态码
    int GAME_STATUS_OVER_WOLF = -1;
    int GAME_STATUS_OVER_GOOD = 1;
    int GAME_STATUS_PROCESS= 0;

    int VOTE_TYPE_CHIEF = 1;
    int VOTE_TYPE_COMMON =2;



    //游戏流程中状态码
    int ROOM_SEARCH_SUCCESS = 0;  //搜索房间并进入
    int ROOM_NOT_EXIST = -1;  //房间不存在
    int ROOM_ALREADY_FULL = -2; //房间已满
    int ROOM_NOT_ENOUGH_NUM = -3; //房间人数不够




}
