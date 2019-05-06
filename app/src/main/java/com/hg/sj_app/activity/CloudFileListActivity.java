package com.hg.sj_app.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hg.sj_app.R;
import com.hg.sj_app.adapter.CloudFileListAdapter;
import com.hg.sj_app.helper.HttpHelper;
import com.hg.sj_app.service.entity.UserFile;
import com.hg.sj_app.values.StaticValues;
import com.hg.ui.dialog.HGLoadDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CloudFileListActivity extends AppCompatActivity {

    private ListView listView;

    private List<UserFile> files = new ArrayList<>();

    private HGLoadDialog hgLoadDialog;

    private String type;
    private Handler handler;

    private HttpHelper httpHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_file_list);
        handler = new Handler();
        httpHelper = new HttpHelper(this, "");
        initView();
    }

    private void initView() {
        listView = findViewById(R.id.file_list_view);
        hgLoadDialog = new HGLoadDialog.Builder(this).setHint("加载中...").setOutside(false).create();
        hgLoadDialog.show();
        type = getIntent().getStringExtra("type");
        getFileList();

        hgLoadDialog.dismiss();
    }

    public void getFileList() {
        httpHelper.get(StaticValues.HOST_URL + "/userFile/getFileList?userId=" + StaticValues.USER_ID+"&type="+type, new Callback() {
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
                                UserFile userFile=new UserFile();
                                JSONObject data=(JSONObject)jsonArray.get(i);
                                userFile.setFileDate(data.getString("fileDate"));
                                userFile.setFileId(data.getString("fileId"));
                                userFile.setFileName(data.getString("fileName"));
                                userFile.setFileSize(data.getDouble("fileSize"));
                                userFile.setFileType(data.getString("fileType"));
                                userFile.setFileUrl(data.getString("fileUrl"));
                                userFile.setUserId(data.getString("userId"));
                                files.add(userFile);
                            }
                            CloudFileListAdapter fileListAdapter = new CloudFileListAdapter(CloudFileListActivity.this, files,handler);
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


    @Override
    protected void onRestart() {
        super.onRestart();
        initView();
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
