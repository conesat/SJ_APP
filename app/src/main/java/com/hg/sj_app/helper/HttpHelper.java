package com.hg.sj_app.helper;

import android.content.Context;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpHelper {
    private String token;
    private Context context;
    private static OkHttpClient okHttpClient;

    public HttpHelper(Context context,String token) {
        this.context = context;
        this.token=token;
        File sdcache = context.getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
        okHttpClient = builder.build();
    }

    public void get(String url, Callback callback) {
        Request request = new Request.Builder().url(url).method("GET", null).addHeader("token",token).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }

    public void post(String url, RequestBody formBody, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .addHeader("token",token)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }



/*
    private void downAsynFile(String url) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(new File("/sdcard/123.jpg"));
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("123", "文件下载成功");
            }
        });
    }*/
}
