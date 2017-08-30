package com.zinglabs.zwerewolf.entity;

import com.zinglabs.zwerewolf.entity.role.UserRole;
import com.zinglabs.zwerewolf.util.GameUtil;

import java.lang.invoke.ConstantCallSite;
import java.util.*;
import java.util.concurrent.BlockingQueue;

/**
 * 房间实体类
 * @author wangtonghe
 * @date 2017/7/24 18:20
 */
public class Room {

    /**
     * 房间id
     */
    private int id;

    /**
     * 房间人数
     */
    private int number;

    /**
     * 模式
     */
    private int modalId;

    /**
     * 房主
     */
    private int owner;

    /**
     * 警长
     */
    private int chief;

    /**
     * 表示第几天
     */
    private volatile int bout;



    /**
     * 房间玩家信息
     */
    private Map<Integer,UserRole> players = new HashMap<>();

    /**
     * 存活列表
     */
    private List<Integer> liveList = new ArrayList<>();

    public List<Integer> getLiveList() {
        return liveList;
    }

    public void updateLiveList(Integer ... deadList) {
        for(Integer userPos:deadList){
            if(liveList.contains(userPos)){
                int userId = GameUtil.getIdByPos(players,userPos);
                players.get(userId).setDead(true);
                liveList.remove(userPos);
            }
        }
    }

    /**
     * 游戏信息
     */
    private Map<Integer,NightInfo> gameInfoMap = new HashMap<>();



    private Map<Integer,SpeakInfo> gameSpeakMap = new HashMap<>();

    public SpeakInfo getGameSpeakMap(int bout) {
       if(gameSpeakMap==null){
           return null;
       }
       return gameSpeakMap.get(bout);
    }

    public void setGameSpeakMap(int bout,SpeakInfo speakInfo) {
        if(gameSpeakMap==null){
            gameSpeakMap = new HashMap<>();
        }
        gameSpeakMap.put(bout,speakInfo);
    }

    /**
     * 狼人杀人列表
     */
    private List<Integer> killLIst = new ArrayList<>();

    public List<Integer> getKillLIst() {
        return killLIst;
    }

    public void addKilled(int pos) {
        killLIst.add(pos);
    }


    public Room(int id, int modalId, int owner) {
        this.id = id;
        this.modalId = modalId;
        this.number =  GameUtil.getNumByModal(modalId);
        this.owner = owner;
    }

    public int getBout() {
        return bout;
    }

    public void setBout(int bout) {
        this.bout = bout;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getModalId() {
        return modalId;
    }

    public void setModalId(int modalId) {
        this.number =  GameUtil.getNumByModal(modalId);
        this.modalId = modalId;
    }

    public Map<Integer, UserRole> getPlayers() {
        return players;
    }

    public void setPlayers(Map<Integer, UserRole> players) {
        this.players = players;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public boolean enterRoom(int userId){
        if(players.containsKey(userId)){
            return true;
        }
        if(players.size()>=this.number){  //房间人数已满
            return false;
        }
        UserRole userRole = new UserRole();
        userRole.setPosition(players.size() + 1);
        userRole.setUserId(userId);
        players.put(userId, userRole);
        liveList.add(userRole.getPosition());
        return true;
    }

    public int getCurNumber(){
        return players.size();
    }


    public Map<Integer, NightInfo> getGameInfoMap() {
        return gameInfoMap;
    }

    public int getChief() {
        return chief;
    }

    public void setChief(int chief) {
        this.chief = chief;
    }

    public void setGameInfoMap(Map<Integer, NightInfo> gameInfoMap) {
        this.gameInfoMap = gameInfoMap;
    }
}
