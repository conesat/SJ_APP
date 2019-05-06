package com.hg.sj_app;

import android.app.Application;

import com.androidev.download.DefaultNotifier;
import com.androidev.download.DownloadManager;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DownloadManager.getInstance().initialize(this, 3);
        DownloadManager.getInstance().setDownloadNotifier(new DefaultNotifier(this));
    }
}
