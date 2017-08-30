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

    //进入房间请求
    short CID_BNS_ENTER_ROOM_REQ =3;

    //进入房间响应
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

    //开始游戏失败
    short CID_GAME_START_FAIL =-1;

    //准备游戏
    short CID_GAME_READY_REQ=1;

    //准备游戏确认
    short CID_GAME_READY_RESP=2;

    //开始游戏请求
    short CID_GAME_START_REQ =3;

    //开始游戏确认
    short CID_GAME_START_RESP =4;

    //他人进入房间
    short CID_GAME_OTHER_ENTER =5;

    //天黑
    short CID_GAME_DARK=6;

    //天亮
    short CID_GAME_DAWN=7;

    //狼人杀人请求
    short CID_GAME_KILL_REQ=8;

    //狼人杀人响应
    short CID_GAME_KILL_RESP=9;

    //狼人杀人结果响应
    short CID_GAME_KILL_RES_RESP=10;

    //女巫救人请求
    short CID_GAME_SAVE_REQ = 11;

    //女巫毒人请求
    short CID_GAME_POISON_REQ = 12;

    //守卫守卫请求
    short CID_GAME_GUARD_REQ = 13;

    //预言家验人请求
    short CID_GAME_VERIFY_REQ = 14;

    //预言家验人响应
    short CID_GAME_VERIFY_RESP = 15;

    //告知女巫狼人杀人信息
    short CID_GAME_NOTIFY_WITCH_KILLED = 16;

    //计时时间到，即请求天亮
    short CID_GAME_TIMER_OVER = 17;

    //请求竞选警长
    short CID_GAME_ASK_CHIEF = 18;

    //请求竞选警长响应
    short CID_GAME_ASK_CHIEF_RESP = 19;

    //取消竞选警长
    short CID_GAME_QUIT_POLICE = 20;

    //取消竞选警长响应
    short CID_GAME_QUIT_POLICE_RESP = 21;

    //开始警上发言
    short CID_GAME_POLICE_START_SPEAKING = 22;

    //警上发言结束
    short CID_GAME_POLICE_SPEAKING_END = 23;

    //开始警长投票
    short CID_GAME_START_CHIEF_VOTE = 24;

    //警下投票
    short CID_GAME_CHIEF_VOTE = 25;

    //警下投票响应
    short CID_GAME_CHIEF_VOTE_RESP = 26;

    //选出警长响应
    short CID_GAME_ELECT_CHIEF_RESP = 27;

    //警长决定发言顺序
    short CID_GAME_CHIEF_DECIDE_SPEAK = 28;

    //开始发言
    short CID_GAME_START_SPEAKING = 29;

    //请求投票
    short CID_GAME_REQ_VOTE = 30;

    //请求警长归票
    short CID_GAME_CHIEF_REQ_SUM_TICKET = 31;

    //警长归票
    short CID_GAME_CHIEF_SUM_TICKET = 32;

    //请求投票响应
    short CID_GAME_REQ_VOTE_RESP = 33;

    //投票
    short CID_GAME_VOTE= 34;

    //投票响应
    short CID_GAME_VOTE_RESP= 35;

    //请求天黑
    short CID_GAME_REQ_DARK = 36;

    //警徽移交
    short CID_GAME_CHANGE_CHIEF = 37;

    //警徽移交响应
    short CID_GAME_CHANGE_CHIEF_RESP = 38;

    //轮到某人发言
    short CID_GAME_TURN_SPEAK = 39;

    //轮到警上发言
    short CID_GAME_TURN_CHIEF_SPEAK = 40;

    //轮到某人聊天结束
    short CID_GAME_TURN_SPEAK_END = 41;

















}
