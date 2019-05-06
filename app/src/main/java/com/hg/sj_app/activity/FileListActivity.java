package com.hg.sj_app.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.hg.sj_app.R;
import com.hg.sj_app.adapter.FileListAdapter;
import com.hg.sj_app.module.FileData;
import com.hg.ui.dialog.HGLoadDialog;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FileListActivity extends AppCompatActivity {

    private ListView listView;

    private List<FileData> files = new ArrayList<>();

    private HGLoadDialog hgLoadDialog;

    private String type;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);
        handler = new Handler();
        initView();
    }

    private void initView() {
        files.clear();
        listView = findViewById(R.id.file_list_view);
        hgLoadDialog = new HGLoadDialog.Builder(this).setHint("加载中...").setOutside(false).create();
        hgLoadDialog.show();
        type = getIntent().getStringExtra("type");
        getFileList(Environment.getExternalStorageDirectory().getPath() + File.separator + "守机" + File.separator + type);
        FileListAdapter fileListAdapter = new FileListAdapter(this, files, handler);
        listView.setAdapter(fileListAdapter);
        hgLoadDialog.dismiss();
    }

    public void getFileList(String strPath) {
        File f = new File(strPath);
        if (f.exists()) {
            File[] fs = f.listFiles();
            for (File file : fs) {
                if (file.isFile()) {
                    FileData fileData = new FileData();
                    fileData.setName(file.getName());
                    fileData.setPath(file.getPath());
                    fileData.setDate(getFileTime(file.getPath()));
                    fileData.setSize(getFileSize(file.length()));
                    fileData.setType(type);
                    files.add(fileData);
                }
            }
        }
    }

    public String getFileTime(String filepath) {
        File f = new File(filepath);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());
    }

    public String getFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        DecimalFormat d = new DecimalFormat("#");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = d.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    public void back(View view) {
        this.finish();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        initView();
    }
}
