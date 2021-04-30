package com.example.sipmobileapp.model;

public class UserResult {

    private String errorCode;
    private String error;
    private UserInfo[] users;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public UserInfo[] getUsers() {
        return users;
    }

    public void setUsers(UserInfo[] users) {
        this.users = users;
    }
}
