package com.androidev.download;


import java.util.List;

public interface DownloadNotifier {

    void notify(List<DownloadInfo> infos);

}
