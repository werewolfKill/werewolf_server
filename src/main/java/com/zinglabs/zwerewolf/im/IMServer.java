package com.zinglabs.zwerewolf.im;



import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.zinglabs.zwerewolf.config.Config;

/**
 *
 * 登录服务，用来获取Server IP 和Port的
 *
 * author: vector.huang
 * date：2016/4/20 20:54
 */
public class IMServer {

    private static IMServer instance;
    private static Lock lock = new ReentrantLock();

    private IMServer(){}
    public static IMServer instance(){
        if(instance == null){
            lock.lock();
            if(instance == null){
                instance = new IMServer();
            }
            lock.unlock();
            lock = null;
        }
        return instance;
    }

    private ThreadServerSocket serverServerSocket;

    /**
     * 运行登录服务
     */
    public void startServer(){
        serverServerSocket = new ThreadServerSocket(Config.SERVER_PORT);
        serverServerSocket.setOnChannelActiveListener((ctx)->{
            System.out.println("Server -- 新连接进来了");
        });
        serverServerSocket.start();
    }

    public void stopServer(){
        serverServerSocket.stopSocket();
    }
}
