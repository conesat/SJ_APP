package com.hg.sj_app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.hg.sj_app.LoginActivity;
import com.hg.sj_app.R;
import com.hg.sj_app.helper.HttpHelper;
import com.hg.sj_app.values.StaticValues;
import com.hg.ui.dialog.HGLoadDialog;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FileManagerActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back;
    private static final int OPEN_FILE = 100;

    private LinearLayout picture;
    private LinearLayout vedio;
    private LinearLayout audio;
    private LinearLayout word;
    private LinearLayout zip;
    private LinearLayout other;

    private ImageView menu;
    private ImageView fileImg;
    private LinearLayout vipLayout;

    private HGLoadDialog hgLoadDialog;
    private HttpHelper httpHelper;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        initView();
    }

    private void initView() {
        back = findViewById(R.id.file_manager_back);
        picture = findViewById(R.id.view_home_file_manager_picture);
        vedio = findViewById(R.id.view_home_file_manager_vedio);
        audio = findViewById(R.id.view_home_file_manager_audio);
        word = findViewById(R.id.view_home_file_manager_word);
        zip = findViewById(R.id.view_home_file_manager_zip);
        other = findViewById(R.id.view_home_file_manager_other);
        menu = findViewById(R.id.file_menu);
        fileImg = findViewById(R.id.file_manager_img);
        vipLayout = findViewById(R.id.file_vip_view);

        back.setOnClickListener(this::onClick);
        picture.setOnClickListener(this::onClick);
        vedio.setOnClickListener(this::onClick);
        audio.setOnClickListener(this::onClick);
        word.setOnClickListener(this::onClick);
        zip.setOnClickListener(this::onClick);
        other.setOnClickListener(this::onClick);
        menu.setOnClickListener(this::onClick);
        handler = new Handler();
        loadData();
        httpHelper = new HttpHelper(this, "");
    }

    private void loadData() {
        vipLayout.setVisibility(View.GONE);
        menu.setImageResource(R.drawable.right);
        fileImg.setImageResource(R.drawable.phone);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String all = getSDTotalSize();
                String avil = getSDAvailableSize();
                int allNum = 0, avilNum = 0;
                if (all.indexOf("GB") > 0) {
                    allNum = (int) (Float.parseFloat(all.replace("GB", "")) * 1000 * 1000);
                } else if (all.indexOf("MB") > 0) {
                    allNum = (int) (Float.parseFloat(all.replace("MB", "")) * 1000);
                } else if (all.indexOf("KB") > 0) {
                    allNum = (int) (Float.parseFloat(all.replace("KB", "")));
                }
                if (avil.indexOf("GB") > 0) {
                    avilNum = (int) (Float.parseFloat(avil.replace("GB", "")) * 1000 * 1000);
                } else if (avil.indexOf("MB") > 0) {
                    avilNum = (int) (Float.parseFloat(avil.replace("MB", "")) * 1000);
                } else if (avil.indexOf("KB") > 0) {
                    avilNum = (int) (Float.parseFloat(avil.replace("KB", "")));
                }
                int finalAllNum = allNum;
                int finalAvilNum = avilNum;
                int vedioNum = getFileNum(Environment.getExternalStorageDirectory().getPath() + File.separator + "守机" + File.separator + "视频");
                int audioNum = getFileNum(Environment.getExternalStorageDirectory().getPath() + File.separator + "守机" + File.separator + "音频");
                int pictureNum = getFileNum(Environment.getExternalStorageDirectory().getPath() + File.separator + "守机" + File.separator + "图片");
                int wordNum = getFileNum(Environment.getExternalStorageDirectory().getPath() + File.separator + "守机" + File.separator + "文档");
                int yswjNum = getFileNum(Environment.getExternalStorageDirectory().getPath() + File.separator + "守机" + File.separator + "压缩文件");
                int otherNum = getFileNum(Environment.getExternalStorageDirectory().getPath() + File.separator + "守机" + File.separator + "其他");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView) findViewById(R.id.flie_manager_alive_num)).setText("本机文件：" + avil + "可用");
                        ((TextView) findViewById(R.id.view_home_file_manager_vedio_num)).setText("(" + vedioNum + ")");
                        ((TextView) findViewById(R.id.view_home_file_manager_audio_num)).setText("(" + audioNum + ")");
                        ((TextView) findViewById(R.id.view_home_file_manager_picture_num)).setText("(" + pictureNum + ")");
                        ((TextView) findViewById(R.id.view_home_file_manager_word_num)).setText("(" + wordNum + ")");
                        ((TextView) findViewById(R.id.view_home_file_manager_zip_num)).setText("(" + yswjNum + ")");
                        ((TextView) findViewById(R.id.view_home_file_manager_other_num)).setText("(" + otherNum + ")");
                        ProgressBar progressBar = findViewById(R.id.flie_manager_alive_num_pro);
                        progressBar.setMax(finalAllNum);
                        progressBar.setProgress(finalAllNum - finalAvilNum);
                    }
                });

            }
        }).start();
    }

    /**
     * 获得SD卡总大小
     *
     * @return
     */
    private String getSDTotalSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(FileManagerActivity.this, blockSize * totalBlocks);
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    private String getSDAvailableSize() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return Formatter.formatFileSize(FileManagerActivity.this, blockSize * availableBlocks);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.file_manager_back:
                this.finish();
                break;
            case R.id.view_home_file_manager_picture:
                toList("图片");
                break;
            case R.id.view_home_file_manager_vedio:
                toList("视频");
                break;
            case R.id.view_home_file_manager_audio:
                toList("音频");
                break;
            case R.id.view_home_file_manager_word:
                toList("文档");
                break;
            case R.id.view_home_file_manager_zip:
                toList("压缩文件");
                break;
            case R.id.view_home_file_manager_other:
                toList("其他");
                break;
            case R.id.file_menu:
                if (vipLayout.getVisibility() == View.VISIBLE) {
                    loadData();
                } else {
                    loadCloud();
                }
                break;
        }
    }

    public void toList(String type) {
        if (vipLayout.getVisibility()==View.GONE) {
            Intent intent = new Intent(FileManagerActivity.this, FileListActivity.class);
            intent.putExtra("type", type);
            startActivity(intent);
        }else {
            Intent intent = new Intent(FileManagerActivity.this, CloudFileListActivity.class);
            intent.putExtra("type", type);
            startActivity(intent);
        }
    }

    public void openFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, OPEN_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case OPEN_FILE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        Intent intent = new Intent(FileManagerActivity.this, AddFileActivity.class);
                        intent.putExtra("uri", uri.toString());
                        intent.putExtra("opr", "add");
                        startActivity(intent);
                    }
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public int getFileNum(String strPath) {
        int num = 0;
        File f = new File(strPath);
        if (f.exists()) {
            File[] fs = f.listFiles();
            for (File file : fs) {
                if (file.isFile()) {
                    num++;
                }
            }
        }
        return num;
    }


    public void loadCloud() {
        if (StaticValues.USER_ID.equals("")){
            startActivity(new Intent(FileManagerActivity.this, LoginActivity.class));
            this.finish();
        }
        hgLoadDialog = new HGLoadDialog.Builder(this).setHint("获取中...").setOutside(false).create();
        hgLoadDialog.show();
        httpHelper.get(StaticValues.HOST_URL + "/userFile/getInfo?userId=" + StaticValues.USER_ID, new Callback() {
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
                        hgLoadDialog.dismiss();
                        if (jsonObject != null && !jsonObject.getString("code").equals("0")) {
                            showToast(jsonObject.getString("data"));
                        } else if (jsonObject.getString("code").equals("0")){
                            fileImg.setImageResource(R.drawable.cloud);
                            vipLayout.setVisibility(View.VISIBLE);
                            menu.setImageResource(R.drawable.back);
                            JSONObject data = JSONObject.parseObject(jsonObject.getString("data"));
                            JSONObject space = JSONObject.parseObject(data.getString("space"));
                            ((TextView) findViewById(R.id.vip_type)).setText(space.getString("spaceType"));
                            ((TextView) findViewById(R.id.flie_manager_alive_num)).setText("云端文件：" + toSize(space.getDouble("spaceSize")) + " 可用");
                            ((TextView) findViewById(R.id.view_home_file_manager_vedio_num)).setText("(" + data.getString("vedioNum") + ")");
                            ((TextView) findViewById(R.id.view_home_file_manager_audio_num)).setText("(" + data.getString("audioNum") + ")");
                            ((TextView) findViewById(R.id.view_home_file_manager_picture_num)).setText("(" + data.getString("picNum") + ")");
                            ((TextView) findViewById(R.id.view_home_file_manager_word_num)).setText("(" + data.getString("wordNum") + ")");
                            ((TextView) findViewById(R.id.view_home_file_manager_zip_num)).setText("(" + data.getString("zipNum") + ")");
                            ((TextView) findViewById(R.id.view_home_file_manager_other_num)).setText("(" + data.getString("otherNum") + ")");
                            ProgressBar progressBar = findViewById(R.id.flie_manager_alive_num_pro);
                            progressBar.setMax((int) (double) space.getDouble("spaceAll"));
                            progressBar.setProgress((int) (double) (space.getDouble("spaceAll") - space.getDouble("spaceSize")));
                        }
                    }
                });
            }
        });
    }

    public String toSize(double size) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (size > 1024 * 1024 * 1024) {
            return df.format(size / (1024 * 1024 * 1024)) + "GB";
        } else if (size > 1024 * 1024) {
            return df.format(size / (1024 * 1024)) + "MB";
        } else if (size > 1024) {
            return df.format(size / (1024)) + "KB";
        } else {
            return size + "B";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (vipLayout.getVisibility() != View.VISIBLE) {
            loadData();
        } else {
            loadCloud();
        }
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
