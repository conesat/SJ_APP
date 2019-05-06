package com.hg.sj_app.service.entity.face;

import java.util.Date;
import lombok.Data;

@Data
public class FaceInfo {
    private Integer id;

    private Integer groupId;

    private String faceId;

    private Date createTime;

    private Date updateTime;

    private String imageUrl;

    private byte[] faceFeature;
}