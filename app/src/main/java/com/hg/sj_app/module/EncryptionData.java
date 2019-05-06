package com.hg.sj_app.module;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class EncryptionData implements Serializable {
    private String path;
    private boolean encryption = false;
    private String authoer;
    private byte[] data;
    private Map<String, String> keys = new HashMap<>();
}
