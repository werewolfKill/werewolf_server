package com.vector.im.entity;

import com.vector.im.constant.ProtocolConstant;

import io.netty.buffer.ByteBuf;

/**
 * author: vector.huang
 * date：2016/4/18 19:08
 */
public class Packet {

    private int userId;//这个消息谁发的

    private int length;//4 具体包的长度，header.length + body.length
    private short serviceId; //2 服务号
    private short commandId; //2 命令号
    private short version; //2 协议版本
    private short reserved; //保留，可用于序列号等
    private ByteBuf body; //数据体

    public Packet() {
    }

    public Packet(int length, short serviceId, short commandId, short version, short reserved, ByteBuf body) {
        this.length = length;
        this.serviceId = serviceId;
        this.commandId = commandId;
        this.version = version;
        this.reserved = reserved;
        this.body = body;
    }

    public Packet(int length, short serviceId, short commandId, ByteBuf body) {
        this.length = length;
        this.serviceId = serviceId;
        this.commandId = commandId;
        version = ProtocolConstant.VERSION;
        reserved = ProtocolConstant.RESERVED;
        this.body = body;
    }


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public short getServiceId() {
        return serviceId;
    }

    public void setServiceId(short serviceId) {
        this.serviceId = serviceId;
    }

    public short getCommandId() {
        return commandId;
    }

    public void setCommandId(short commandId) {
        this.commandId = commandId;
    }

    public short getVersion() {
        return version;
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public short getReserved() {
        return reserved;
    }

    public void setReserved(short reserved) {
        this.reserved = reserved;
    }

    public ByteBuf getBody() {
        return body;
    }

    public void setBody(ByteBuf body) {
        this.body = body;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
