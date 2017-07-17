package com.vector.im.entity;

import io.netty.channel.Channel;

/**
 *
 * 用户+Channel
 *
 * author: vector.huang
 * date：2016/4/21 1:15
 */
public class UserChannel {

    private int userId;
    private String username;

    private Channel channel;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
