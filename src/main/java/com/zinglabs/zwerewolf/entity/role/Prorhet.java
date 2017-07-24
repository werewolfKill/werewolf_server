package com.zinglabs.zwerewolf.entity.role;

/**
 * 预言家
 * @author wangtonghe
 * @date 2017/7/24 19:12
 */
public class Prorhet implements Role {
    private static final String NAME = "预言家";

    private int number;

    public Prorhet(int number) {
        this.number = number;
    }

    public static String getNAME() {
        return NAME;
    }
}
