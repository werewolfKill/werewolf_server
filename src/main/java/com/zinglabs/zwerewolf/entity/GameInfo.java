package com.zinglabs.zwerewolf.entity;

import java.util.*;

/**
 * 游戏信息
 *
 * @author wangtonghe
 * @date 2017/8/2 08:08
 */
public class GameInfo {

    /**
     * 警上是否投过票
     */
    private boolean isChiefVote;


    public boolean isChiefVote() {
        return isChiefVote;
    }

    public void setChiefVote(boolean isChiefVote) {
        this.isChiefVote = isChiefVote;
    }

    /**
     * 某天是否投票
     */
    private Map<Integer,Boolean> isVoteMap =  new HashMap<>();

    /**
     * 某天请求次数
     */
    private Map<Integer,Integer> askVoteMap = new HashMap<>();

    public int getAskVote(int bout) {
        if(askVoteMap==null){
            return 0;
        }
        return askVoteMap.get(bout);
    }

    public void addAskVote(int bout) {
        if(askVoteMap==null){
           askVoteMap = new HashMap<>();
        }
       Integer num = askVoteMap.putIfAbsent(bout,1);
       if(num!=null){
           askVoteMap.put(bout,num+1);
       }
    }

    public Boolean getIsVote(int bout) {
        if(isVoteMap==null){
            return false;
        }
        return isVoteMap.get(bout);
    }

    public void setIsVote(int bout,boolean isVote) {
        this.isVoteMap.put(bout,isVote);
    }

    /**
     * 狼人杀人信息
     */
    private Map<Integer, Integer> killInfo = new HashMap<>();

    /**
     * 选举信息
     */
    private Map<Integer,Integer> voteInfo = new HashMap<>();


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
        this.speakEndNum+=1;
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
       putInfo(killInfo,userId);
    }


    /**
     * 添加投票信息
     *
     * @param userId 用户id
     */
    public void putVoteInfo(int userId) {
        putInfo(voteInfo,userId);
    }

    public int getVoteWinner(){
        return getVoteResult(voteInfo);
    }

    private void putInfo(Map<Integer, Integer> voteMap,int userId){
        Integer num = voteMap.putIfAbsent(userId, 1);
        if (num != null) {
            voteMap.put(userId, num + 1);
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

    private int getVoteResult(Map<Integer, Integer> voteMap) {
        if(voteMap==null||voteMap.size()==0){
            return 0;
        }
        List<Map.Entry<Integer, Integer>> sortMap = new ArrayList<>(voteMap.entrySet());
        Collections.sort(sortMap, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        int first = sortMap.get(0).getValue();
        if (sortMap.size() == 1) {
            return sortMap.get(0).getKey();
        }
        int second = sortMap.get(1).getValue();
        if (first == second) {
            voteMap.clear();
            return 0;
        } else {
            int code = sortMap.get(0).getValue();
            voteMap.clear();
            return code;
        }
    }

}
