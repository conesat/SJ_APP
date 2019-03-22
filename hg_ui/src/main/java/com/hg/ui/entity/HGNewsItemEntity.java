package com.hg.ui.entity;

import java.util.List;

public class HGNewsItemEntity {

    private String senderIconUrl;//发送者头像

    private String senderName;//发送者名称

    private String sendTime;//发送时间

    private String newsDetail;//文本内容

    private List<String> medias;//媒体内容

    private String mediaType;//媒体类型

    private Long commentNum;//评论数

    private Long forwardNum;//转发数

    private Long recommendNum;//推荐数

    public void setSenderIconUrl(String senderIconUrl) {
        this.senderIconUrl = senderIconUrl;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public void setNewsDetail(String newsDetail) {
        this.newsDetail = newsDetail;
    }

    public void setMedias(List<String> medias) {
        this.medias = medias;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public void setCommentNum(Long commentNum) {
        this.commentNum = commentNum;
    }

    public void setForwardNum(Long forwardNum) {
        this.forwardNum = forwardNum;
    }

    public void setRecommendNum(Long recommendNum) {
        this.recommendNum = recommendNum;
    }

    public String getSenderIconUrl() {
        return senderIconUrl;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSendTime() {
        return sendTime;
    }

    public String getNewsDetail() {
        return newsDetail;
    }

    public List<String> getMedias() {
        return medias;
    }

    public String getMediaType() {
        return mediaType;
    }

    public Long getCommentNum() {
        return commentNum;
    }

    public Long getForwardNum() {
        return forwardNum;
    }

    public Long getRecommendNum() {
        return recommendNum;
    }

    public HGNewsItemEntity() {
    }

    public HGNewsItemEntity(String senderIconUrl, String senderName, String sendTime, String newsDetail, List<String> medias, String mediaType, Long commentNum, Long forwardNum, Long recommendNum) {
        this.senderIconUrl = senderIconUrl;
        this.senderName = senderName;
        this.sendTime = sendTime;
        this.newsDetail = newsDetail;
        this.medias = medias;
        this.mediaType = mediaType;
        this.commentNum = commentNum;
        this.forwardNum = forwardNum;
        this.recommendNum = recommendNum;
    }
}
