package com.hg.ui.entity;

public class HGNewsMediaEntity {
    private String reviewImgUrl;//预览地址
    private String sourceImgUrl;//实际地址

    public String getReviewImgUrl() {
        return reviewImgUrl;
    }

    public void setReviewImgUrl(String reviewImgUrl) {
        this.reviewImgUrl = reviewImgUrl;
    }

    public String getSourceImgUrl() {
        return sourceImgUrl;
    }

    public void setSourceImgUrl(String sourceImgUrl) {
        this.sourceImgUrl = sourceImgUrl;
    }

    public HGNewsMediaEntity(String reviewImgUrl, String sourceImgUrl) {
        this.reviewImgUrl = reviewImgUrl;
        this.sourceImgUrl = sourceImgUrl;
    }
}
