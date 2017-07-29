package com.zinglabs.zwerewolf.util;

import com.zinglabs.zwerewolf.config.Config;

/**
 * @author wangtonghe
 * @date 2017/7/29 12:15
 */
public class RoomUtil {

    public static int getNumByModal(int modalId) {
        int number ;
        switch (modalId) {
            case Config.MODEL_9:
                number = 9;
                break;
            case Config.MODEL_10:
                number = 10;
                break;
            case Config.MODEL_12_YNLB:
                number = 12;
                break;
            case Config.MODEL_12_YNLS:
                number = 12;
                break;
            default:
                number = 12;
                break;
        }
        return number;
    }
}
