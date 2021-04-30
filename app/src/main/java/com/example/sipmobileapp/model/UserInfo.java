package com.example.sipmobileapp.model;

public class UserInfo {

    private String UserLoginKey;
    private int UserID;
    private String User_Name;
    private int UserGroupID;
    private String GroupName;
    private String UserFullName;
    private boolean Disable;

    public String getUserLoginKey() {
        return UserLoginKey;
    }

    public void setUserLoginKey(String userLoginKey) {
        UserLoginKey = userLoginKey;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getUser_Name() {
        return User_Name;
    }

    public void setUser_Name(String user_Name) {
        User_Name = user_Name;
    }

    public int getUserGroupID() {
        return UserGroupID;
    }

    public void setUserGroupID(int userGroupID) {
        UserGroupID = userGroupID;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String getUserFullName() {
        return UserFullName;
    }

    public void setUserFullName(String userFullName) {
        UserFullName = userFullName;
    }

    public boolean isDisable() {
        return Disable;
    }

    public void setDisable(boolean disable) {
        Disable = disable;
    }
}
