package com.zinglabs.zwerewolf.entity;

/**
 * 解析后的消息实体
 * @author wangtonghe
 * @date 2017/7/24 09:28
 */
public class ByteBufMsg {

    /**
     * 消息发送者id
     */
    private int fromId;

    /**
     * 消息长度
     */
    private int length;

    /**
     * 房间id
     */
    private int roomId;

    /**

     * 消息内容
     */
    private Object content;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
