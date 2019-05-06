package com.hg.sj_app.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.hg.sj_app.R;
import com.hg.sj_app.helper.HttpHelper;
import com.hg.sj_app.service.entity.UserInfo;
import com.hg.sj_app.util.FileUtilcll;
import com.hg.sj_app.values.StaticValues;
import com.hg.ui.dialog.HGButtomDialog;
import com.hg.ui.dialog.HGLoadDialog;
import com.hg.ui.listener.HGBottomDialogOnClickListener;
import com.hg.ui.view.CircularImageView;
import com.hg.ui.view.RoundLinearlayout;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class InfoEditActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int ALBUM_RESULT_CODE = 103;
    private static final int REQUEST_PERMISSIONS = 102;
    private static final int CROP_RESULT_CODE = 104;
    private HGButtomDialog userSexDialog;
    private HGButtomDialog userImgDialog;
    private ImageView back;
    private RoundLinearlayout settingUserImg;
    private RoundLinearlayout settingUserSex;
    private RoundLinearlayout settingBirthday;
    private RoundLinearlayout settingUserName;
    private RoundLinearlayout settingUserReallyName;
    private RoundLinearlayout settingSave;

    private TextView userBirthday;
    private TextView userName;
    private TextView userReallyName;
    private TextView userSex;
    private TextView userPhone;
    private TextView joinDate;
    private CircularImageView userImg;

    private String userImgUrlPath;//头像地址
    private String userImgUrlPathResponse="";//上传服务器后地址

    private Handler handler;
    private HGLoadDialog hgLoadDialog;
    private HttpHelper httpHelper;


    private SharedPreferences preference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_edit);
        initView();
    }

    private void initView() {
        back = findViewById(R.id.edit_info_back);
        settingUserImg = findViewById(R.id.setting_user_img);
        settingUserSex = findViewById(R.id.setting_user_sex);
        settingBirthday = findViewById(R.id.setting_user_birthday);
        settingUserReallyName = findViewById(R.id.setting_user_reallyname);
        settingUserName = findViewById(R.id.setting_user_name);
        settingSave = findViewById(R.id.setting_save);
        userImg = findViewById(R.id.setting_user_img_view);
        joinDate=findViewById(R.id.setting_user_join_text);
        userPhone=findViewById(R.id.setting_user_phone_text);

        userBirthday = findViewById(R.id.setting_user_birthday_text);
        userName = findViewById(R.id.setting_user_name_text);
        userReallyName = findViewById(R.id.setting_user_reallyname_text);
        userSex = findViewById(R.id.setting_user_sex_text);

        back.setOnClickListener(this::onClick);
        settingUserImg.setOnClickListener(this::onClick);
        settingUserSex.setOnClickListener(this::onClick);
        settingBirthday.setOnClickListener(this::onClick);
        settingUserName.setOnClickListener(this::onClick);
        settingUserReallyName.setOnClickListener(this::onClick);
        settingSave.setOnClickListener(this::onClick);

        preference = getSharedPreferences("user", MODE_PRIVATE);
        userBirthday.setText(preference.getString("userBirthday", ""));
        userName.setText(preference.getString("userName", ""));
        userReallyName.setText(preference.getString("userReallyName", ""));
        userSex.setText(preference.getString("userSex", ""));
        userPhone.setText(preference.getString("userPhone", ""));
        joinDate.setText(preference.getString("userJoinDate", ""));

        if (preference.getString("userImgUrl", "")!=null&&!preference.getString("userImgUrl", "").equals("")){
            Picasso.with(this).load(StaticValues.HOST_URL+"/userFile/getFile?path="+preference.getString("userImgUrl", "")).into(userImg);
        }
        handler = new Handler();
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_info_back:
                this.finish();
                break;
            case R.id.setting_user_img:
                showUserImgSetting();
                break;
            case R.id.setting_user_sex:
                showUserSexSetting();
                break;
            case R.id.setting_user_birthday:
                showUserBirthdaySetting();
                break;
            case R.id.setting_user_name:
                showUserNameSetting();
                break;
            case R.id.setting_user_reallyname:
                showUserReallyNameSetting();
                break;
            case R.id.setting_save:
                saveSetting();
                break;
        }
    }

    private void saveSetting() {
        httpHelper = new HttpHelper(this, "");
        updateUserInfo();
    }


    public void showInputDialog(String text, OnInputConfirmListener onInputConfirmListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_input, null);
        EditText input = view.findViewById(R.id.dialog_input);
        input.setText(text);
        TextView confirm = view.findViewById(R.id.dialog_confirm);
        builder.setView(view);
        AlertDialog inputDialog = builder.show();
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDialog.dismiss();
                onInputConfirmListener.onConfirm(input.getText().toString());
            }
        });
    }

    interface OnInputConfirmListener {
        void onConfirm(String input);
    }

    private void showUserReallyNameSetting() {
        showInputDialog(userReallyName.getText().toString(), new OnInputConfirmListener() {
            @Override
            public void onConfirm(String input) {
                userReallyName.setText(input);
            }
        });
    }

    private void showUserNameSetting() {
        showInputDialog(userName.getText().toString(), new OnInputConfirmListener() {
            @Override
            public void onConfirm(String input) {
                userName.setText(input);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showUserBirthdaySetting() {
        //获得当前日期
        final Calendar calendar = Calendar.getInstance(Locale.CHINA);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                userBirthday.setText(year + "-" + month + "-" + dayOfMonth);
            }
        }, year, month, day);//创建弹框
        dialog.show();//显示弹框
    }

    public void showUserImgSetting() {
        if (userImgDialog == null) {
            initPermission();
            List<String> menus = new ArrayList<>();
            menus.add("选取图片");
            userImgDialog = new HGButtomDialog(this, menus);
            userImgDialog.setHgBottomDialogOnClickListener(new HGBottomDialogOnClickListener() {
                @Override
                public void onClick(View v, int i) {
                    if (i == 0) {
                        openSysAlbum();
                    }
                    userImgDialog.dismiss();
                }
            });
        }
        userImgDialog.show();
    }

    public void showUserSexSetting() {
        if (userSexDialog == null) {
            List<String> menus = new ArrayList<>();
            menus.add("男");
            menus.add("女");
            userSexDialog = new HGButtomDialog(this, menus);
            userSexDialog.setHgBottomDialogOnClickListener(new HGBottomDialogOnClickListener() {
                @Override
                public void onClick(View v, int i) {
                    userSex.setText(menus.get(i));
                    userSexDialog.dismiss();
                }
            });
        }
        userSexDialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //成功
                    showToast("用户授权相机权限");
                } else {
                    // 勾选了不再询问
                    showToast("用户拒绝相机权限");
                    /**
                     * 跳转到 APP 详情的权限设置页
                     *
                     * 可根据自己的需求定制对话框，点击某个按钮在执行下面的代码
                     */
                    Intent intent = getAppDetailSettingIntent(InfoEditActivity.this);
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 获取 APP 详情页面intent
     *
     * @return
     */
    public static Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return localIntent;
    }

    /**
     * 打开相册
     */
    private void openSysAlbum() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, ALBUM_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ALBUM_RESULT_CODE:
                // 相册
                cropPic(data.getData());
                break;
            case CROP_RESULT_CODE:
                // 裁剪时,这样设置 cropIntent.putExtra("return-data", true); 处理方案如下
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap bitmap = bundle.getParcelable("data");
                        userImg.setImageBitmap(bitmap);
                        // 把裁剪后的图片保存至本地 返回路径
                        userImgUrlPath = FileUtilcll.saveFile(this, "crop.jpg", bitmap);
                    }
                }
                break;
        }

    }


    /**
     * 裁剪图片
     *
     * @param data
     */
    private void cropPic(Uri data) {
        if (data == null) {
            return;
        }
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(data, "image/*");

        // 开启裁剪：打开的Intent所显示的View可裁剪
        cropIntent.putExtra("crop", "true");
        // 裁剪宽高比
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        // 裁剪输出大小
        cropIntent.putExtra("outputX", 320);
        cropIntent.putExtra("outputY", 320);
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, CROP_RESULT_CODE);
    }

    /**
     * 初始化相机相关权限
     * 适配6.0+手机的运行时权限
     */
    private void initPermission() {
        String[] permissions = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        //检查权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // 之前拒绝了权限，但没有点击 不再询问 这个时候让它继续请求权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                showToast("用户曾拒绝打开相机权限");
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
            } else {
                //注册相机权限
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
            }
        }
    }



    public void updateUserInfo() {
        if(userName.getText().toString().equals("")){
            showToast("请输入昵称");
            return;
        }
        hgLoadDialog = new HGLoadDialog.Builder(this).setHint("正在修改...").setOutside(false).create();
        hgLoadDialog.show();

        MultipartBody.Builder builder=new MultipartBody.Builder();
        if (userImgUrlPath!=null){
            builder.addFormDataPart("file", "xx.png",
                    RequestBody.create(MediaType.parse("multipart/form-data"), new File(userImgUrlPath)));
        }
        RequestBody requestBody = builder
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId", StaticValues.USER_ID)
                .addFormDataPart("userBirthday", userBirthday.getText().toString())
                .addFormDataPart("userName", userName.getText().toString())
                .addFormDataPart("userReallyName", userReallyName.getText().toString())
                .addFormDataPart("userSex", userSex.getText().toString())
                .build();

        httpHelper.post(StaticValues.HOST_URL + "/user/update", requestBody, new Callback() {
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

                System.out.println(jsonObject.toJSONString()+"xxxxx");

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        hgLoadDialog.dismiss();
                        if (!jsonObject.getString("code").equals("0")) {
                            showToast(jsonObject.getString("data"));
                        } else {
                            UserInfo userInfo = JSONObject.parseObject(jsonObject.getString("data"), UserInfo.class);
                            SharedPreferences.Editor editor = preference.edit();
                            editor.putString("userName", userInfo.getUserName());
                            editor.putString("userImgUrl", userInfo.getUserImgUrl());
                            editor.putString("userBirthday", userInfo.getUserBirthday());
                            editor.putString("userReallyName", userInfo.getUserReallyName());
                            editor.putString("userSex", userInfo.getUserSex());
                            editor.putString("userJoinDate", userInfo.getUserJoinDate());
                            editor.commit();//提交数据
                            showToast("更新完成");
                        }
                    }
                });
            }
        });

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
