package com.zinglabs.zwerewolf.im;



import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.zinglabs.zwerewolf.entity.UserChannel;

/**
 * 暂存用户信息
 * author: vector.huang
 * date：2016/4/18 20:42
 */
public class IMChannelGroup {
    private static int sId = 100;

    private Map<Integer,UserChannel> channels;

    private static IMChannelGroup instance;
    private static Lock lock = new ReentrantLock();
    public static IMChannelGroup instance(){
        if(instance == null){
            lock.lock();
            if(instance == null){
                instance = new IMChannelGroup();
            }
            lock.unlock();
            lock = null;
        }
        return instance;
    }

    private IMChannelGroup(){
        channels = new HashMap<>();
    }

    public synchronized  int put(UserChannel channel){
        sId++;
        channels.put(sId,channel);
        return sId;
    }

    public synchronized void remove(int id){
        channels.remove(id);
    }

    public synchronized UserChannel get(int id){
        return channels.get(id);
    }

    public int size() {
        return channels.size();
    }

    public Map<Integer, UserChannel> getChannels() {
        return channels;
    }
}
