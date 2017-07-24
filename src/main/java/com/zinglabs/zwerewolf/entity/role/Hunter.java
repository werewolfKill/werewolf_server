package com.zinglabs.zwerewolf.entity.role;

/**
 * 猎人
 * @author wangtonghe
 * @date 2017/7/24 18:44
 */
public class Hunter implements Role {

    private static final String NAME = "猎人";

    private int number;

    public Hunter(int number) {
        this.number = number;
    }

    public static String getNAME() {
        return NAME;
    }
}
