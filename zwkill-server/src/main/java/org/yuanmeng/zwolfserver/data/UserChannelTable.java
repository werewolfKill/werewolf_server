package org.yuanmeng.zwolfserver.data;

import org.yuanmeng.zwolfserver.entity.UserChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 暂存用户关系
 *
 * @author wangtonghe
 * @since 2018/10/6 08:42
 */
public class UserChannelTable {

    private static Map<Integer, UserChannel> table = new ConcurrentHashMap<>();

    public static void addUser(int userId, UserChannel userChannel) {
        table.putIfAbsent(userId, userChannel);
    }

    public static void removeUser(int userId) {
        table.remove(userId);
    }

    public static UserChannel getUser(int userId) {
        return table.get(userId);
    }

    public static int getTotal() {
        return table.size();
    }

}
