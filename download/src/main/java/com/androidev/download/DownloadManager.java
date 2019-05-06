package com.androidev.download;

import android.content.Context;
/*

import junit.framework.Assert;
*/

import java.util.List;

/**
 * Created by 4ndroidev on 16/10/6.
 */
public class DownloadManager {

    public final static String INTENT_ACTION_DOWNLOAD = "com.androidev.download";

    private static class DownloadManagerHolder {
        private static DownloadManager instance = new DownloadManager();
    }

    private DownloadEngine engine;

    private DownloadManager() {
    }

    public void initialize(Context context, int masTask) {
        engine = new DownloadEngine(masTask);
        engine.initialize(context);
    }

    public static DownloadManager getInstance() {
        return DownloadManagerHolder.instance;
    }

    public void destroy() {
      //  Assert.assertNotNull(engine);
        engine.destroy();
        engine = null;
    }

    public DownloadTask.Builder newTask(long id, String url, String name) {
      //  Assert.assertNotNull(engine);
        return new DownloadTask.Builder(engine).id(id).url(url).name(name);
    }

    public DownloadTask createTask(DownloadInfo info, DownloadListener listener) {
        return new DownloadTask(engine, info, listener);
    }

    public void addInterceptor(Interceptor interceptor) {
     //   Assert.assertNotNull(engine);
        engine.addInterceptor(interceptor);
    }

    public void addDownloadJobListener(DownloadJobListener downloadJobListener) {
      //  Assert.assertNotNull(engine);
        engine.addDownloadJobListener(downloadJobListener);
    }

    public void removeDownloadJobListener(DownloadJobListener downloadJobListener) {
     //   Assert.assertNotNull(engine);
        engine.removeDownloadJobListener(downloadJobListener);
    }

    public void setDownloadNotifier(DownloadNotifier downloadNotifier) {
     //   Assert.assertNotNull(engine);
        engine.setDownloadNotifier(downloadNotifier);
    }

    public List<DownloadTask> getAllTasks() {
      //  Assert.assertNotNull(engine);
        return engine.getAllTasks();
    }

    public List<DownloadInfo> getAllInfo() {
        return engine.getAllInfo();
    }

    public void delete(DownloadInfo info) {
       // Assert.assertNotNull(engine);
        engine.delete(info);
    }

    public boolean isActive() {
     //   Assert.assertNotNull(engine);
        return engine.isActive();
    }

    public interface Interceptor {
        void updateDownloadInfo(DownloadInfo info);
    }

}
