package com.zinglabs.zwerewolf.constant;

/**
 * author: vector.huang
 * date：2016/4/18 19:31
 */
public interface ProtocolConstant {

    short VERSION = 1; //版本号
    short RESERVED = 0;//保留字段

    /**
     * 用户服务
     */
    //服务号，表示消息服务
    short SID_USER = 1;

    //用户登录请求
    short CID_USER_LOGIN_REQ = 1;

    //用户登录响应
    short CID_USER_LOGIN_RESP = 2;

    //用户上线请求（登录app）
    short CID_USER_ONLINE_REQ = 3;

    ////用户上线响应（登录app）
    short CID_USER_ONLINE_RESP = 4;


    /**
     * 消息服务
     */
    //服务号，表示消息服务
    short SID_MSG = 2;

    //群聊文字消息请求
    short CID_MSG_TEXT_REQ = 1;

    //群聊文字消息响应
    short CID_MSG_TEXT_RESP = 2;

    //群聊语音消息请求
    short CID_MSG_VOICE_REQ = 3;

    //群聊语音中断请求
    short CID_MSG_VOICE_INTERRUPT_REQ = 4;

    //群聊语音结束请求
    short CID_MSG_VOICE_END_REQ = 5;

    //群聊语音响应
    short CID_MSG_VOICE_RESP = 6;

    //群聊语音结束响应
    short CID_MSG_VOICE_END_RESP = 7;

    /**
     *业务(business)服务
     */
    short SID_BNS=3;

    //创建房间请求
    short CID_BNS_CRE_ROOM_REQ=1;

    //创建房间响应
    short CID_BNS_CRE_ROOM_RESP=2;

    //随机进入房间请求
    short CID_BNS_ENTER_ROOM_REQ =3;

    //随机进入房间响应
    short CID_BNS_ENTER_ROOM_RESP =4;

    //搜索房间请求
    short CID_BNS_FIND_ROOM_REQ=5;

    //搜索房间响应
    short CID_BNS_FIND_ROOM_RESP=6;


    /**
     * 游戏流程服务
     */
    //服务号，表示游戏流程
    short SID_GAME = 4;



    //准备游戏
    short CID_GAME_READY_REQ=1;

    //准备游戏确认
    short CID_GAME_READY_RESP=2;

    //开始游戏请求
    short CID_GAME_START_REQ =3;

    //开始游戏确认
    short CID_GAME_START_RESP =4;

    //开始游戏失败
    short CID_GAME_START_FAIL =-1;

    //天黑
    short CID_GAME_DARK=5;

    //天亮
    short CID_GAME_DAWN=6;

    //狼人杀人请求
    short CID_GAME_KILL_REQ=7;

    //狼人杀人响应
    short CID_GAME_KILL_RESP=8;

    //狼人杀人结果响应
    short CID_GAME_KILL_RES_RESP=9;

    //女巫救人请求
    short CID_GAME_SAVE_REQ = 10;

    //女巫毒人请求
    short CID_GAME_POISON_REQ = 11;

    //守卫守卫请求
    short CID_GAME_GUARD_REQ = 12;

    //预言家验人请求
    short CID_GAME_VERIFY_REQ = 13;

    //预言家验人响应
    short CID_GAME_VERIFY_RESP = 14;

    //告知女巫狼人杀人信息
    short CID_GAME_NOTIFY_WITCH_KILLED = 15;

    //计时时间到，即请求天亮
    short CID_GAME_TIMER_OVER = 16;










}
