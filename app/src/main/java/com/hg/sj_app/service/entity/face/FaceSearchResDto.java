package com.hg.sj_app.service.entity.face;

import lombok.Data;

@Data
public class FaceSearchResDto {
    private Integer similarValue;
    private String image;
    private FaceInfo faceInfo;
    private FindInfo findInfo;
}
