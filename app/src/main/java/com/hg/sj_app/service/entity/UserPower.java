package com.hg.sj_app.service.entity;


public class UserPower {
    private String id;

    private String powerName;

    private String powerCode;

    private String powerDetail;

    private String powerType;


    public UserPower() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPowerName() {
        return powerName;
    }

    public void setPowerName(String powerName) {
        this.powerName = powerName;
    }

    public String getPowerCode() {
        return powerCode;
    }

    public void setPowerCode(String powerCode) {
        this.powerCode = powerCode;
    }

    public String getPowerDetail() {
        return powerDetail;
    }

    public void setPowerDetail(String powerDetail) {
        this.powerDetail = powerDetail;
    }

    public String getPowerType() {
        return powerType;
    }

    public void setPowerType(String powerType) {
        this.powerType = powerType;
    }
}