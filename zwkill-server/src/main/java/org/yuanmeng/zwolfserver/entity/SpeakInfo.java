package org.yuanmeng.zwolfserver.entity;

import java.util.Timer;
import java.util.concurrent.BlockingQueue;

/**
 * @author wangtonghe
 * @date 2017/8/30 21:14
 */
public class SpeakInfo {

    /**
     * 发言顺序列表
     */
    private BlockingQueue<Integer> speaker;

    /**
     * 类型，分为警上发言和普通发言
     */
    private int type;

    /**
     * 定时器指针
     */
    private Timer timer;



    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public Integer getGameSpeak() {
        if (speaker == null) {
            return null;
        }
        return speaker.poll();
    }

    public SpeakInfo(BlockingQueue<Integer> speaker, int type, Timer timer) {
        this.speaker = speaker;
        this.type = type;
        this.timer = timer;
    }
}
