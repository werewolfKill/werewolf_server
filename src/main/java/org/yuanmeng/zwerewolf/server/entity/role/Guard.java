package org.yuanmeng.zwerewolf.server.entity.role;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangtonghe
 * @date 2017/8/6 16:42
 */
public class Guard  implements Role{
    public static final String NAME = "守卫";

    private List<Integer> guardList = new ArrayList<>();

    public static String getNAME() {
        return NAME;
    }

    public List<Integer> getGuardList() {
        return guardList;
    }

    public boolean setGuardian(int guard){
        if(guardList.size()==0){
            guardList.add(guard);
        }else{
            if(guardList.get(guardList.size()-1)==guard){
                return false;
            }
        }
        return true;
    }
}
