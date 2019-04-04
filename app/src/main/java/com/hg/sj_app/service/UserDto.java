package com.hg.sj_app.service;


import com.hg.sj_app.service.entity.UserInfo;
import com.hg.sj_app.service.entity.UserLogin;

public class UserDto {
    private UserInfo userInfo;
    private UserLogin userLogin;

    public UserDto() {
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userInfo=" + userInfo +
                ", userLogin=" + userLogin +
                '}';
    }
}

