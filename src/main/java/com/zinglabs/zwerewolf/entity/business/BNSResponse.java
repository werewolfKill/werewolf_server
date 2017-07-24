package com.zinglabs.zwerewolf.entity.business;

/**
 * 封装的业务响应实体
 * @author wangtonghe
 * @date 2017/7/24 18:08
 */
public class BNSResponse {

    /**
     * 服务id
     */
    private short serviceId;

    /**
     * command
     */
    private short command;

    /**
     * 业务用int表示响应码，具体含义与具体命令有关,不用设0
     */
    private int reply;

    /**
     * 发送者id
     */
    private int fromId;

    private int roomId;

    public BNSResponse(short serviceId, short command, int fromId , int roomId, int reply) {
        this.serviceId = serviceId;
        this.command = command;
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

    public short getCommand() {
        return command;
    }

    public void setCommand(short command) {
        this.command = command;
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
