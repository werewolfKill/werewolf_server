package com.zinglabs.zwerewolf.util;

import com.zinglabs.zwerewolf.config.Config;
import com.zinglabs.zwerewolf.entity.role.Role;
import com.zinglabs.zwerewolf.entity.role.Villager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangtonghe
 * @date 2017/7/29 12:15
 */
public class RoomUtil {

    public static int getNumByModal(int modalId) {
        int number;
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

    public static List<Integer> getRolesByModalId(int modalId) {
        List<Integer> roles = new ArrayList<>();
        List<Integer> retRoles = new ArrayList<>();

        roles.addAll(Arrays.asList(Config.ROLE_CODE_OF_VILLAGER, Config.ROLE_CODE_OF_VILLAGER,
                Config.ROLE_CODE_OF_VILLAGER));  //3个村民

        roles.addAll(Arrays.asList(Config.ROLE_CODE_OF_WOLF, Config.ROLE_CODE_OF_WOLF,
                Config.ROLE_CODE_OF_WOLF));   //3个狼

        roles.add(Config.ROLE_CODE_OF_HUNTSMAN);
        roles.add(Config.ROLE_CODE_OF_PROPHET);
        roles.add(Config.ROLE_CODE_OF_WITCH);   //3个神

        switch (modalId) {

            case Config.MODEL_9:
                retRoles = roles;
                break;
            case Config.MODEL_10:
                roles.add(Config.ROLE_CODE_OF_VILLAGER);
                retRoles = roles;
                break;
            case Config.MODEL_12_YNLB:
                roles.add(Config.ROLE_CODE_OF_VILLAGER);
                roles.add(Config.ROLE_CODE_OF_GUARD);
                roles.add(Config.ROLE_CODE_OF_WOLF);
                retRoles = roles;
                break;
            case Config.MODEL_12_YNLS:
                roles.add(Config.ROLE_CODE_OF_VILLAGER);
                roles.add(Config.ROLE_CODE_OF_IDIOT);
                roles.add(Config.ROLE_CODE_OF_WOLF);
                retRoles = roles;
                break;
        }
        return retRoles;


    }
}
