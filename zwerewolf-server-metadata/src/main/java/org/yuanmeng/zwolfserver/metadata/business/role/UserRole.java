package org.yuanmeng.zwolfserver.metadata.business.role;

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
