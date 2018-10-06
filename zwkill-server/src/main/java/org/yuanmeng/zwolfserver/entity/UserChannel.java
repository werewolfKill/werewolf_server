package org.yuanmeng.zwolfserver.entity;

import lombok.Data;
import org.yuanmeng.zwolfserver.entity.role.Role;
import io.netty.channel.Channel;

/**
 * 用户 channel对应表
 *
 * @author wangtonghe
 * @since 2018/10/6 08:07
 */
@Data
public class UserChannel {

    private int userId;

    private String userName;

    private Channel channel;

    private Role role;

    private Room room;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserChannel that = (UserChannel) o;

        if (userId != that.userId) {
            return false;
        }
        return channel != null ? channel.equals(that.channel) : that.channel == null;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (channel != null ? channel.hashCode() : 0);
        return result;
    }
}
