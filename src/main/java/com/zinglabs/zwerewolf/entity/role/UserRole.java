package com.zinglabs.zwerewolf.entity.role;

/**
 * 用户角色类
 * @author wangtonghe
 * @date 2017/7/29 12:42
 */
public class UserRole {

    private int userId;

    private String userName;

    private int roleId;

    private Role role;

    /**
     * 玩家在房间的位置编号
     */
    private int position;

    /**
     * 是否准备好
     */
    private boolean isReady;

    /**
     * 是否存活
     */
    private boolean isDead;

    /**
     * 是否被女巫救
     */
    private boolean isSaved;

    /**
     * 是否被女巫毒
     */
    private boolean isPoisoned;

    /**
     * 是否被狼杀
     */
    private boolean isKilled;

    /**
     * 是否被守卫
     */
    private boolean isGuarded;

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public boolean isPoisoned() {
        return isPoisoned;
    }

    public void setPoisoned(boolean poisoned) {
        isPoisoned = poisoned;
    }

    public boolean isKilled() {
        return isKilled;
    }

    public void setKilled(boolean isKilled) {
        this.isKilled = isKilled;
    }

    public boolean isGuarded() {
        return isGuarded;
    }

    public void setGuarded(boolean guarded) {
        isGuarded = guarded;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRole userRole = (UserRole) o;

        return userId == userRole.userId;

    }

    @Override
    public int hashCode() {
        return userId;
    }
}
