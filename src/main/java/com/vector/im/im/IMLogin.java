package com.vector.im.im;

import com.vector.im.config.Config;
import com.vector.im.manager.IMLoginManager;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

/**
 *
 * 登录服务，用来获取Server IP 和Port的
 *
 * author: vector.huang
 * date：2016/4/20 20:54
 */
public class IMLogin {

    private static IMLogin instance;
    private static Lock lock = new ReentrantLock();

    private IMLogin(){}
    public static IMLogin instance(){
        if(instance == null){
            lock.lock();
            if(instance == null){
                instance = new IMLogin();
            }
            lock.unlock();
            lock = null;
        }
        return instance;
    }

    private ThreadServerSocket loginServerSocket;

    /**
     * 运行登录服务
     */
    public void startServer(){
        loginServerSocket = new ThreadServerSocket(Config.LOGIN_PORT);
        loginServerSocket.setOnChannelActiveListener((ctx)->{
            System.out.println("LoginServer -- 新连接进来了");
            //发送服务主机和端口
            IMLoginManager.outIpPort(ctx.channel(),Config.SERVER_HOST,Config.SERVER_PORT);
        });
        loginServerSocket.start();
    }

    public void stopServer(){
        loginServerSocket.stopSocket();
    }
}
