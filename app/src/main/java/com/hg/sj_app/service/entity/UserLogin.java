package com.hg.sj_app.service.entity;

public class UserLogin {
    private String userId;
    private String userPhone;
    private String userPassword;
    private Double loginLongitude;
    private Double loginDimension;
    private String loginMacId;
    private String loginTime;

    public UserLogin() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Double getLoginLongitude() {
        return loginLongitude;
    }

    public void setLoginLongitude(Double loginLongitude) {
        this.loginLongitude = loginLongitude;
    }

    public Double getLoginDimension() {
        return loginDimension;
    }

    public void setLoginDimension(Double loginDimension) {
        this.loginDimension = loginDimension;
    }

    public String getLoginMacId() {
        return loginMacId;
    }

    public void setLoginMacId(String loginMacId) {
        this.loginMacId = loginMacId;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return "UserLogin{" +
                "userId='" + userId + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", loginLongitude=" + loginLongitude +
                ", loginDimension=" + loginDimension +
                ", loginMacId='" + loginMacId + '\'' +
                ", loginTime='" + loginTime + '\'' +
                '}';
    }
}