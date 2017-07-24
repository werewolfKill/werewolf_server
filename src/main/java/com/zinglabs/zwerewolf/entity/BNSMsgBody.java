package com.zinglabs.zwerewolf.entity;

/**
 * @author wangtonghe
 * @date 2017/7/24 18:08
 */
public class BNSMsgBody {

    /**
     * 服务id
     */
    private short serviceId;

    /**
     * commonId
     */
    private short commonId;

    /**
     * 业务用int表示响应码，具体含义与具体命令有关,不用设0
     */
    private int reply;

    /**
     * 发送者id
     */
    private int fromId;

    private int roomId;

    public BNSMsgBody(short serviceId, short commonId,  int fromId ,int roomId,int reply) {
        this.serviceId = serviceId;
        this.commonId = commonId;
        this.reply = reply;
        this.roomId = roomId;
        this.fromId = fromId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public short getServiceId() {
        return serviceId;
    }

    public void setServiceId(short serviceId) {
        this.serviceId = serviceId;
    }

    public short getCommonId() {
        return commonId;
    }

    public void setCommonId(short commonId) {
        this.commonId = commonId;
    }

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }
}
