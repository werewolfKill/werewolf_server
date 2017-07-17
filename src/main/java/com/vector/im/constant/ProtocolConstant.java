package com.vector.im.constant;

/**
 * author: vector.huang
 * date：2016/4/18 19:31
 */
public interface ProtocolConstant {

    short VERSION = 1; //版本号
    short RESERVED = 0;//保留字段

    /**
     * 测试服务
     */
    short SID_TEST = -1;// 测试服务
    short CID_TEST_TEST_REQ = 1;
    short CID_TEST_TEST_RSP = 2;


    /**
     * 登录服务
     */
    short SID_LOGIN = 0;//登录
    short CID_LOGIN_OUT = 1; //发送登录IP和端口
    short CID_LOGIN_IN = 2; //获取成功成功


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
