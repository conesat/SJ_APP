package com.hg.sj_app.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.hg.sj_app.R;
import com.hg.sj_app.module.EncryptionData;
import com.hg.sj_app.util.EncryptionFile;
import com.hg.sj_app.util.FileTypeUtil;
import com.hg.sj_app.util.FileUtilcll;
import com.hg.ui.dialog.HGLoadDialog;
import com.hg.ui.view.RoundLinearlayout;
import com.hg.ui.view.SquareImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AddFileActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean aouth = true;
    private boolean delete = false;

    private SquareImageView img;
    private EditText fileName;

    private Switch mSwitch;
    private Switch dSwitch;
    private LinearLayout layout;
    private LinearLayout userList;
    private RoundLinearlayout addUser;
    private RoundLinearlayout finish;
    private File file;
    private String type;
    private List<String> users = new ArrayList<>();
    private HGLoadDialog hgLoadDialog;
    private Handler handler;
    private int num = 0;
    private final int maxNum = 100;

    private EncryptionData encryptionData;
    private String opr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_file);
        initView();
    }

    private void initView() {
        fileName = findViewById(R.id.add_file_name);
        img = findViewById(R.id.add_file_img);
        mSwitch = findViewById(R.id.add_file_add_user_open);
        dSwitch = findViewById(R.id.add_file_add_user_delete);
        layout = findViewById(R.id.add_file_add_user_layout);
        userList = findViewById(R.id.add_file_add_user_list);
        addUser = findViewById(R.id.add_file_add_user_button);
        finish = findViewById(R.id.add_file_add_user_finish);
        finish.setOnClickListener(this::onClick);
        handler = new Handler();
        opr=getIntent().getStringExtra("opr");
        if (opr.equals("add")) {
            String uri = getIntent().getStringExtra("uri");
            file = FileUtilcll.uriToFile(Uri.parse(uri), this);
            type = FileTypeUtil.getType(file.getPath());

        } else {
            hgLoadDialog = new HGLoadDialog.Builder(this).setHint("读取中...").setOutside(false).create();
            hgLoadDialog.show();
            file = new File(getIntent().getStringExtra("uri"));
            type = getIntent().getStringExtra("type");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    EncryptionFile encryptionFile = new EncryptionFile(AddFileActivity.this);
                    encryptionData = encryptionFile.decryptFile2Data(file.getPath());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            userList.removeAllViews();
                            Iterator iter = encryptionData.getKeys().entrySet().iterator();
                            while (iter.hasNext()) {
                                Map.Entry entry = (Map.Entry) iter.next();
                                LinearLayout view = (LinearLayout) LayoutInflater.from(AddFileActivity.this).inflate(R.layout.add_file_user_edit, null);
                                ((EditText) view.getChildAt(0)).setText(entry.getKey().toString());
                                view.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((LinearLayout) v.getParent().getParent()).removeView((View) v.getParent());
                                    }
                                });
                                userList.addView(view);
                            }
                            hgLoadDialog.dismiss();
                        }
                    });
                }
            }).start();
        }
        dSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    delete = true;
                } else {
                    delete = false;
                }
            }
        });
        fileName.setText(file.getName().substring(0,file.getName().lastIndexOf(".")));
        switch (type) {
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
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    aouth = true;
                    layout.setVisibility(View.VISIBLE);
                } else {
                    aouth = false;
                    layout.setVisibility(View.GONE);
                }
            }
        });
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num >= maxNum) {
                    showToast("已达上限");
                } else {
                    LinearLayout view = (LinearLayout) LayoutInflater.from(AddFileActivity.this).inflate(R.layout.add_file_user_edit, null);
                    view.getChildAt(1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((LinearLayout) v.getParent().getParent()).removeView((View) v.getParent());
                        }
                    });
                    userList.addView(view);
                }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_file_add_user_finish:
                if (fileName.getText().toString().equals("")){
                    showToast("请输入文件名");
                    return;
                }else {
                    if (!delete&&new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "守机" + File.separator + type+File.separator+fileName.getText().toString()+".hgd").exists()){
                        showToast("文件名已存在");
                        return;
                    }
                }
                hgLoadDialog = new HGLoadDialog.Builder(this).setHint("处理中...").setOutside(false).create();
                hgLoadDialog.show();
                if (aouth) {
                    for (int i = 0; i < userList.getChildCount(); i++) {
                        String phone = ((EditText) ((LinearLayout) userList.getChildAt(i)).getChildAt(0)).getText().toString();
                        if (!phone.equals("")) {
                            users.add(phone);
                        }
                    }
                }
                EncryptionFile encryptionFile = new EncryptionFile(this, file.getPath(), users);
                encryptionFile.setProcessListner(new EncryptionFile.ProcessListner() {
                    @Override
                    public void finish(String path, boolean done) {
                        hgLoadDialog.dismiss();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (done) {
                                    showToast("加密完成");
                                    if (delete&&!(Environment.getExternalStorageDirectory().getPath() + File.separator + "守机" + File.separator + type+File.separator+fileName.getText().toString()+".hgd").equals(file.getPath())) {
                                        file.delete();
                                    }
                                    AddFileActivity.this.finish();
                                } else {
                                    showToast("加密失败");
                                }
                            }
                        });

                    }
                });
                boolean sdCardExist = Environment.getExternalStorageState()
                        .equals(android.os.Environment.MEDIA_MOUNTED);
                if (sdCardExist) {
                    File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "守机" + File.separator + type);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    if (opr.equals("add")){
                        encryptionFile.encryptionFile(file.getPath(),fileName.getText().toString());
                    }else {
                        encryptionFile.setPath(encryptionData.getPath());
                        encryptionFile.encryptionFile(file.getPath(),encryptionData.getData(),fileName.getText().toString());
                    }
                }
                break;
        }
    }
}
