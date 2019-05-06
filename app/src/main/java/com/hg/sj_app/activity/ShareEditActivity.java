package com.hg.sj_app.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.hg.sj_app.R;
import com.hg.sj_app.helper.HttpHelper;
import com.hg.sj_app.values.StaticValues;
import com.hg.ui.dialog.HGLoadDialog;
import com.hg.ui.view.RoundLinearlayout;
import com.hg.ui.view.SquareImageView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ShareEditActivity extends AppCompatActivity {
    private SquareImageView img;
    private TextView fileName;
    private Switch mSwitch;
    private TextView shareCode;
    private String code;
    private RoundLinearlayout shareCommit;
    private Integer time = 0;


    private HGLoadDialog hgLoadDialog;
    private HttpHelper httpHelper;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_edit);
        handler = new Handler();
        httpHelper = new HttpHelper(this, "");
        initView();
    }

    private void initView() {
        img = findViewById(R.id.file_img);
        fileName = findViewById(R.id.file_name);
        mSwitch = findViewById(R.id.file_share_time);
        shareCode = findViewById(R.id.share_code);
        shareCommit = findViewById(R.id.share_build);

        fileName.setText(StaticValues.USER_FILE.getFileName().substring(0, StaticValues.USER_FILE.getFileName().length() - 4));
        switch (StaticValues.USER_FILE.getFileType()) {
            case "图片":
                img.setImageResource(R.drawable.picture);
                break;
            case "文档":
                img.setImageResource(R.drawable.word);
                break;
            case "视频":
                img.setImageResource(R.drawable.vedio);
                break;
            case "音频":
                img.setImageResource(R.drawable.audio);
                break;
            case "压缩文件":
                img.setImageResource(R.drawable.zip);
                break;
            case "其他":
                img.setImageResource(R.drawable.other);
                break;
        }
        shareCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code == null) {
                    showToast("确认共享后，即可复制提取码。");
                } else {
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", code);
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    showToast("提取码已复制");
                }
            }
        });
        shareCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSwitch.isChecked()){
                    time=1;
                }else {
                    time=0;
                }
                hgLoadDialog = new HGLoadDialog.Builder(ShareEditActivity.this).setHint("请稍等...").setOutside(false).create();
                hgLoadDialog.show();
                httpHelper.get(StaticValues.HOST_URL + "/userFile/share?shareCode=&fileId=" + StaticValues.USER_FILE.getFileId() + "&shareTime="+time+ "&userId="+StaticValues.USER_ID, new Callback() {
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
                                if (jsonObject != null && !jsonObject.getString("code").equals("0")) {
                                    showToast(jsonObject.getString("data"));
                                } else if (jsonObject.getString("code").equals("0")) {
                                    JSONObject data = JSONObject.parseObject(jsonObject.getString("data"));
                                    if (data==null){
                                        showToast("这个文件你已经共享过了");
                                    }else {
                                        showToast("已创建");
                                        code = data.getString("shareCode");
                                        shareCode.setText(code);
                                    }
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    public void back(View view) {
        this.finish();
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
