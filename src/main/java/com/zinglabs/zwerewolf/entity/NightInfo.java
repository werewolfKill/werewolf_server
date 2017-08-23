package com.zinglabs.zwerewolf.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wangtonghe
 * @date 2017/8/6 19:02
 */
public class NightInfo {

    /**
     * 是否请求过进入天黑
     */
    private volatile boolean isAskNight;


    private int killId;

    private int guardianId;

    private int saveId;

    private int poisonId;

    //    private
    public boolean isAskNight() {
        return isAskNight;
    }

    public void setAskNight(boolean askNight) {
        isAskNight = askNight;
    }

    public int getKillId() {
        return killId;
    }

    public void setKillId(int killId) {
        this.killId = killId;
    }

    public int getGuardianId() {
        return guardianId;
    }

    public void setGuardianId(int guardianId) {
        this.guardianId = guardianId;
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

    public List<Integer> getDeadList() {
        List<Integer> list = new ArrayList<>(2);
        if (killId != saveId && killId != guardianId || killId == saveId && killId == guardianId) {
            list.add(killId);
        }
        if (poisonId > 0) {
            list.add(poisonId);
        }
        return list;
    }

}
