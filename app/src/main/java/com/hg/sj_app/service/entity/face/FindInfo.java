package com.hg.sj_app.service.entity.face;

import java.util.Date;
import lombok.Data;

@Data
public class FindInfo {
    private String faceId;

    private String sex;

    private Date birthday;

    private String city;

    private String name;

    private String idcard;

    private String type;

    private String detial;

    private String sender;

    private String sendCity;

    private Integer state;
}