package com.hg.sj_app.service.entity;

public class UserInfo {
    private String userId;
    private String userName;
    private String userPhone;
    private String userSex;
    private String userBirthday;
    private String userReallyName;
    private String userImgUrl;
    private String userJoinDate;

    public UserInfo() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getUserReallyName() {
        return userReallyName;
    }

    public void setUserReallyName(String userReallyName) {
        this.userReallyName = userReallyName;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public String getUserJoinDate() {
        return userJoinDate;
    }

    public void setUserJoinDate(String userJoinDate) {
        this.userJoinDate = userJoinDate;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userSex='" + userSex + '\'' +
                ", userBirthday='" + userBirthday + '\'' +
                ", userReallyName='" + userReallyName + '\'' +
                ", userImgUrl='" + userImgUrl + '\'' +
                ", userJoinDate='" + userJoinDate + '\'' +
                '}';
    }
}