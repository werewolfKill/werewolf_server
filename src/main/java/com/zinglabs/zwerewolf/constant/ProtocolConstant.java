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
     * 游戏流程服务
     */
    //服务号，表示游戏流程
    short SID_GAME = 3;

    //准备游戏
    short CID_GAME_READY=1;

    //进入天黑
    short CID_GAME_DARK=2;

    //天亮
    short CID_GAME_DAWN=3;

    //狼人杀人
    short CID_GAME_KILL=4;

    //确认狼人杀人信息
    short CID_GAME_KILL_DONE=5;

    //略


}
