package org.yuanmeng.zwerewolf.server.entity.role;

/**
 * 女巫
 * @author wangtonghe
 * @date 2017/7/24 19:14
 */
public class Witch implements Role{

    /**
     * 救人id
     */
    private int  saveId;

    /**
     * 毒人id
     */
    private int poisonId;

    public static final String NAME = "女巫";

    public static String getNAME() {
        return NAME;
    }

    public int getSaveId() {
        return saveId;
    }

    public void setSaveId(int saveId) {
        this.saveId = saveId;
    }

    public int getPoisonId() {
        return poisonId;
    }

    public void setPoisonId(int poisonId) {
        this.poisonId = poisonId;
    }
}
