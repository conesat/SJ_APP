package com.hg.sj_app.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 4ndroidev on 17/4/20.
 */

public class AssetsHelper {

    public static String readAsString(Context context, String name) {
        AssetManager assetManager = context.getAssets();
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            is = assetManager.open(name);
            baos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            return new String(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
