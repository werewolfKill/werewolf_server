package com.vector.im;

import com.vector.im.app.App;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * author: vector.huang
 * date：2016/4/18 1:15
 */
public class Boot implements SignalHandler{

    public void boot() {
        App.instance().create();
    }

    @Override
    public void handle(Signal signal) {
        App.instance().destroy();
    }

    public static void main(String[] args) {
        new Boot().boot();
    }
}



