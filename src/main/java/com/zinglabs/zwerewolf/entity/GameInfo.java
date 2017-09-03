package com.zinglabs.zwerewolf.entity;

import java.util.*;

/**
 * 游戏信息
 *
 * @author wangtonghe
 * @date 2017/8/2 08:08
 */
public class GameInfo {



    public static final double DEFAULT_MARK = 1;

    public static final double DEFAULT_CHIEF_MARK = 1.5;

    /**
     * 警上是否投过票
     */
    private boolean isChiefVote;

    /**
     * 警上是否已发言
     */
    private boolean isChiefSpeak;

    public boolean isChiefSpeak() {
        return isChiefSpeak;
    }

    public void setChiefSpeak(boolean chiefSpeak) {
        isChiefSpeak = chiefSpeak;
    }

    public boolean isChiefVote() {
        return isChiefVote;
    }

    public void setChiefVote(boolean isChiefVote) {
        this.isChiefVote = isChiefVote;
    }

    /**
     * 某天是否投票
     */
    private Map<Integer, Boolean> isVoteMap = new HashMap<>();

    /**
     * 某天狼人是否确定杀人信息
     */
    private Map<Integer,Boolean> isKillMap = new HashMap<>();

    /**
     * 某天请求投票次数
     */
    private Map<Integer, Integer> askVoteMap = new HashMap<>();

    public int getAskVote(int bout) {
        if (askVoteMap == null) {
            return 0;
        }
        return askVoteMap.get(bout);
    }

    public void addAskVote(int bout) {
        if (askVoteMap == null) {
            askVoteMap = new HashMap<>();
        }
        Integer num = askVoteMap.putIfAbsent(bout, 1);
        if (num != null) {
            askVoteMap.put(bout, num + 1);
        }
    }

    public Boolean getIsVote(int bout) {
        if (isVoteMap == null) {
            return false;
        }
        return isVoteMap.get(bout);
    }
    public Boolean getIsKill(int bout) {
        if (isKillMap == null) {
            return false;
        }
        return isKillMap.get(bout);
    }

    public void setIsVote(int bout, boolean isVote) {
        this.isVoteMap.put(bout, isVote);
    }
    public void setIsKill(int bout, boolean isKill) {
        this.isKillMap.put(bout, isKill);
    }

    /**
     * 狼人杀人信息
     */
    private Map<Integer, Double> killInfo = new HashMap<>();

    /**
     * 投票信息
     */
    private Map<Integer, Double> voteInfo = new HashMap<>();

    private Map<Integer,Map<Integer,Integer>> voteDetails = new HashMap<>();


    public Map<Integer,Integer>  getVoteDetails(int bout){
       return voteDetails.get(bout);
    }

    public void setVoteDetails(int bout,int voter,int voted){
        if(voteDetails.get(bout)==null){
            voteDetails.put(bout,new HashMap<>());
        }
        voteDetails.get(bout).put(voter,voted);
    }




    /**
     * 竞选警长列表
     */
    private List<Integer> voteChiefs = new ArrayList<>();

    /**
     * 放弃竞选列表
     */
    private List<Integer> quitChiefs = new ArrayList<>();

    private int speakEndNum;

    public int getSpeakEndNum() {
        return speakEndNum;
    }

    public void addSpeakEndNum() {
        this.speakEndNum += 1;
    }

    public List<Integer> getQuitChiefs() {
        return quitChiefs;
    }

    public void addQuitChiefs(Integer userId) {
        this.quitChiefs.add(userId);
    }

    public List<Integer> getChiefVotes() {
        return voteChiefs;
    }

    public void addChiefVotes(Integer voteId) {
        this.voteChiefs.add(voteId);
    }

    public void quitChiefVotes(Integer voteId) {
        this.voteChiefs.remove(voteId);
    }

    public int getVotePoliceNum() {
        return voteChiefs.size();
    }


    /**
     * 添加杀人信息
     *
     * @param userId 用户id
     */
    public void putKillInfo(int userId) {
        putInfo(killInfo, userId,DEFAULT_MARK);
    }


    /**
     * 添加投票信息
     *
     * @param userId 用户id
     */
    public void putVoteInfo(int userId) {
        putInfo(voteInfo, userId,DEFAULT_MARK);
    }


    /**
     * 添加投票信息
     *
     * @param userId 用户id
     */
    public void putVoteInfo(int userId,double mark) {
        putInfo(voteInfo, userId,mark);
    }

    public int getVoteWinner() {
        return getVoteResult(voteInfo);
    }

    private void putInfo(Map<Integer, Double> voteMap, int userId,double mark) {

        Double num = voteMap.putIfAbsent(userId, mark);
        if (num != null) {
            voteMap.put(userId, num + mark);
        }

    }

    /**
     * 获取目前几个狼人已发送杀人请求
     */
    public int getKillNumber() {
        return killInfo.size();

    }


    /**
     * 返回狼人杀人信息
     *
     * @return 0为意见持平，其他为玩家id
     */
    public int getKilled() {
        return getVoteResult(this.killInfo);
    }

    private int getVoteResult(Map<Integer, Double> voteMap) {
        if (voteMap == null || voteMap.size() == 0) {
            return 0;
        }
        List<Map.Entry<Integer, Double>> sortMap = new ArrayList<>(voteMap.entrySet());
        Collections.sort(sortMap, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        int code =  sortMap.get(0).getKey();
        voteMap.clear();
        return code;
    }

}
