package com.hg.sj_app.service.entity;

import lombok.Data;
@Data
public class UserFile {
    private String fileId;

    private String userId;

    private String fileName;

    private String fileType;

    private Double fileSize;

    private String fileUrl;

    private String fileDate;

    public UserFile() {
    }

    protected boolean canEqual(Object other) {
        return other instanceof com.hg.sj_app.service.entity.UserFile;
    }
}