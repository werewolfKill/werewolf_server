package com.vector.im.app;

import com.vector.im.im.IMLogin;
import com.vector.im.im.IMServer;
import com.vector.im.im.ThreadServerSocket;

/**
 * author: vector.huang
 * date：2016/4/18 1:18
 */
public class App {

    private static App instance =  new App();

    private App(){}

    public static App instance(){
        return instance;
    }

    public void create(){
        //开始服务
        IMLogin.instance().startServer();
        IMServer.instance().startServer();
    }

    public void destroy(){
        IMLogin.instance().stopServer();
        IMServer.instance().stopServer();

    }

}
