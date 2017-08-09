package com.zinglabs.zwerewolf.entity;

import com.zinglabs.zwerewolf.entity.role.Role;
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

    private Role role;

    private Room room;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserChannel that = (UserChannel) o;

        if (userId != that.userId) return false;
        return channel != null ? channel.equals(that.channel) : that.channel == null;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (channel != null ? channel.hashCode() : 0);
        return result;
    }
}
