package org.yuanmeng.zwolfserver.entity.role;

/**
 * @author wangtonghe
 * @date 2017/8/6 16:44
 */
public class Idiot implements Role{
    public static final String NAME = "白痴";


    /**
     *是否死过，为真则不能投票
     */
    private boolean isDead;

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public static String getNAME() {
        return NAME;
    }
}
