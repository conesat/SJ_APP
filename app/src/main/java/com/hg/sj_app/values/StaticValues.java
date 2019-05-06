package com.hg.sj_app.values;

import android.graphics.Bitmap;
import android.net.Uri;

import com.alibaba.fastjson.JSONObject;
import com.hg.sj_app.service.entity.UserFile;
import com.hg.ui.entity.HGNewsItemEntity;

public class StaticValues {
    //  public static String HOST_URL="http://172.20.10.3:9900";
    public static String USER_ID = "";
    public static String HOST_URL = "http://192.168.1.100:9900";
    public static String FACE_SERVICE_URL = "http://192.168.1.100:8889";
    public static Uri FACE_URI = null;

    public static Bitmap FACE_SCANNING_BITMAP=null;
    public static JSONObject FACE_SCANNING_JSON=null;

    //被点击的动态
    public static HGNewsItemEntity hgNewsItemEntity;

    //将要共享的文件
    public static UserFile USER_FILE;
}
