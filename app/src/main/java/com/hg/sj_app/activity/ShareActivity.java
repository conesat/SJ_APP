package com.hg.sj_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.androidev.download.DownloadManager;
import com.androidev.download.DownloadTask;
import com.hg.sj_app.R;
import com.hg.sj_app.adapter.CloudFileListAdapter;
import com.hg.sj_app.adapter.FileListAdapter;
import com.hg.sj_app.adapter.ShareFileListAdapter;
import com.hg.sj_app.helper.HttpHelper;
import com.hg.sj_app.service.entity.UserFile;
import com.hg.sj_app.service.entity.UserFileShare;
import com.hg.sj_app.values.StaticValues;
import com.hg.ui.dialog.HGLoadDialog;
import com.hg.ui.view.RoundLinearlayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ShareActivity extends AppCompatActivity {

    private EditText code;
    private RoundLinearlayout getFile;
    private List<UserFileShare> files=new ArrayList<>();
    private HGLoadDialog hgLoadDialog;
    private Handler handler;
    private HttpHelper httpHelper;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        handler = new Handler();
        httpHelper = new HttpHelper(this, "");
        initView();
    }

    private void initView() {
        code = findViewById(R.id.share_code);
        getFile = findViewById(R.id.get_share_file);
        listView = findViewById(R.id.my_share);
        getFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code.getText().toString().equals("")) {
                    showToast("请输入提取码");
                    return;
                } else if (code.getText().toString().length() < 6) {
                    showToast("提取码为6位数");
                    return;
                }
                hgLoadDialog = new HGLoadDialog.Builder(ShareActivity.this).setHint("提取中...").setOutside(false).create();
                hgLoadDialog.show();
                httpHelper.get(StaticValues.HOST_URL + "/userFile/getShare?code=" + code.getText().toString(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                hgLoadDialog.dismiss();
                                showToast("连接失败");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        JSONObject jsonObject = JSONObject.parseObject(response.body().string());
                        System.out.println(jsonObject);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                hgLoadDialog.dismiss();
                                if (jsonObject == null&&!jsonObject.getString("code").equals("0")) {
                                    showToast(jsonObject.getString("data"));
                                } else {
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    if (data==null) {
                                        showToast("共享文件不存在");
                                    } else{
                                        DownloadManager controller = DownloadManager.getInstance();

                                        DownloadTask task = controller.newTask(new Date().getTime(), StaticValues.HOST_URL + "/userFile/getFile?path=" + data.getString("fileUrl")
                                                , data.getString("fileName")).create();
                                        task.start();

                                        showToast("已添加到下载列表");
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
        getFileList();
    }

    private void getFileList() {
        hgLoadDialog = new HGLoadDialog.Builder(this).setHint("加载中...").setOutside(false).create();
        hgLoadDialog.show();
        httpHelper.get(StaticValues.HOST_URL + "/userFile/getShareList?userId=" + StaticValues.USER_ID, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        hgLoadDialog.dismiss();
                        showToast("连接失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject jsonObject = JSONObject.parseObject(response.body().string());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        files.clear();
                        hgLoadDialog.dismiss();
                        if (jsonObject != null && !jsonObject.getString("code").equals("0")) {
                            showToast(jsonObject.getString("data"));
                        } else {
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            for (int i=0;i<jsonArray.size();i++){
                                UserFileShare userFile=new UserFileShare();
                                JSONObject data=(JSONObject)jsonArray.get(i);
                                userFile.setFileName(data.getString("fileName"));
                                userFile.setShareCode(data.getInteger("shareCode"));
                                files.add(userFile);
                            }
                            ShareFileListAdapter fileListAdapter = new ShareFileListAdapter(ShareActivity.this, files, handler);
                            listView.setAdapter(fileListAdapter);
                        }
                    }
                });
            }
        });

    }

    public void back(View view) {
        this.finish();
    }

    public void toDownload(View view) {
        startActivity(new Intent(ShareActivity.this, DownloadActivity.class));
    }

    Toast toast;

    public void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            toast.cancel();
            toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
