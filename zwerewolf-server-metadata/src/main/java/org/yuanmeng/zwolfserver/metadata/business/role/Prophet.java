package org.yuanmeng.zwolfserver.metadata.business.role;

import java.util.HashMap;
import java.util.Map;

/**
 * 预言家
 * @author wangtonghe
 * @date 2017/7/24 19:12
 */
public class Prophet implements Role {

    private Map<Integer,Boolean> verifyMap = new HashMap<>();

    private static final String NAME = "预言家";

    public static String getNAME() {
        return NAME;
    }

    public Map<Integer, Boolean> getVerifyMap() {
        return verifyMap;
    }

    public void setVerifyMap(int userId,boolean isGood) {
       verifyMap.put(userId,isGood);
    }
}
