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

    /**
     * 玩家在房间的位置编号
     */
    private int position;

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
