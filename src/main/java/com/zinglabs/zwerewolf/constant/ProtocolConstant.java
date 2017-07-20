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
    short SID_USER = 1;
    short CID_USER_LOGIN_REQ = 1;
    short CID_USER_LOGIN_RESP = 2;
    short CID_USER_ONLINE_REQ = 3;
    short CID_USER_ONLINE_RESP = 4;


    /**
     * 消息服务
     */
    short SID_MSG = 2;
    short CID_MSG_SEND_SINGLE_REQ = 1;
    short CID_MSG_RECEIVE_SINGLE_OUT = 2;


}
