package com.hg.sj_app.service.entity;


public class UserRelation {
    private String id;

    private String userId;

    private String anotherUserId;

    private String relationType;

    public UserRelation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAnotherUserId() {
        return anotherUserId;
    }

    public void setAnotherUserId(String anotherUserId) {
        this.anotherUserId = anotherUserId;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }
}