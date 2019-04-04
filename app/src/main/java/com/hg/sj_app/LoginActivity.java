package com.hg.sj_app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.hg.sj_app.activity.MainActivity;
import com.hg.sj_app.helper.HttpHelper;
import com.hg.sj_app.service.ResponseInfoDto;
import com.hg.sj_app.service.UserDto;
import com.hg.sj_app.values.StaticValues;
import com.hg.ui.dialog.HGLoadDialog;
import com.hg.ui.view.RoundLinearlayout;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static int CALL_PHONE_CODE = 101;

    private Handler handler;

    private LinearLayout loginLayout;
    private LinearLayout registerLayout;

    private LinearLayout layout;
    private ImageView bgView;

    private SharedPreferences preference;

    //登录
    private RoundLinearlayout login;
    private RoundLinearlayout register;

    //注册
    private RoundLinearlayout registerSubmit;
    private RoundLinearlayout cencleRegister;

    private HttpHelper httpHelper;

    private HGLoadDialog hgLoadDialog;

    private TextView phone;//手机
    private TextView password;//密码
    private TextView vrifyNum;//验证码
    private TextView getVrifyNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
            getWindow().setAttributes(lp);
            //全屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            //无title
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //全屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_login);
        initView();
        httpHelper = new HttpHelper(this, "");
        handler = new Handler();
    }

    private void initView() {
        bgView = findViewById(R.id.loading_bg);
        layout = findViewById(R.id.loading_view_layout);
        preference = getSharedPreferences("user", MODE_PRIVATE);
        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 10) {
            bgView.setImageResource(R.drawable.good_night_img);
        } else if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 17) {
            ((TextView) findViewById(R.id.loding_big_text)).setText("晚上好");
            ((TextView) findViewById(R.id.loding_min_text)).setText("熬过了黑夜就是黎明");
            bgView.setImageResource(R.drawable.good_night_img);
        } else {

        }


        Handler handler = new Handler();
        if (preference.getString("userPhone", "").equals("")) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loginLayout = findViewById(R.id.login_layout);
                    registerLayout = findViewById(R.id.register_layout);
                    initLoginView();
                    layout.setVisibility(View.VISIBLE);
                    Animation animation = new AlphaAnimation(0f, 1.0f);
                    animation.setDuration(2000);
                    layout.startAnimation(animation);
                }
            }, 1500);
        } else {
            StaticValues.USER_ID=preference.getString("userId", "");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    LoginActivity.this.finish();
                }
            }, 2500);
        }
    }

    private void initLoginView() {
        login = findViewById(R.id.login_layout_login);
        register = findViewById(R.id.login_layout_register);

        registerSubmit = findViewById(R.id.register_layout_register);
        cencleRegister = findViewById(R.id.register_layout_cancel);

        register.setOnClickListener(this);
        login.setOnClickListener(this);

        cencleRegister.setOnClickListener(this);
        registerSubmit.setOnClickListener(this);

        phone = findViewById(R.id.login_user_phone);
        password = findViewById(R.id.login_user_password);
        vrifyNum = findViewById(R.id.register_verify_num);
        getVrifyNum = findViewById(R.id.register_get_verify_num);

        getVrifyNum.setOnClickListener(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            verifyPermission();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_layout_register:
                loginLayout.setVisibility(View.GONE);
                registerLayout.setVisibility(View.VISIBLE);
                phone.setText(getPhone());
                phone.setEnabled(false);
                break;
            case R.id.register_layout_cancel:
                registerLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
                phone.setEnabled(true);
                break;
            case R.id.login_layout_login:
                login();
                break;
            case R.id.register_get_verify_num:
                vrifyNum.setText(UUID.randomUUID().toString().substring(0, 4));
                break;
            case R.id.register_layout_register:
                register();
                break;
        }
    }


    public void login() {

        if (phone.getText().toString().equals("")) {
            showToast("请输入手机号");
            return;
        } else if (password.getText().toString().equals("")) {
            showToast("请输入密码");
            return;
        } else {
            String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone.getText());
            boolean isMatch = m.matches();
            if (phone.getText().length() != 11 || !isMatch) {
                showToast("请填入正确的手机号");
                return;
            }
        }
        hgLoadDialog = new HGLoadDialog.Builder(this).setHint("正在登陆...").setOutside(false).create();
        hgLoadDialog.show();

        RequestBody requestBody = new FormBody.Builder()
                .add("userPassword", password.getText().toString())
                .add("userPhone", phone.getText().toString())
                .build();
        httpHelper.post(StaticValues.HOST_URL + "/user/login", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        hgLoadDialog.cancel();
                        showToast("连接失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseInfoDto responseInfoDto = JSONObject.parseObject(response.body().string(), ResponseInfoDto.class);
                if (responseInfoDto.getCode() != 100) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            hgLoadDialog.cancel();
                            showToast(responseInfoDto.getMsg());
                        }
                    });
                } else {
                    UserDto userDto = JSONObject.parseObject(responseInfoDto.getDetail(), UserDto.class);
                    SharedPreferences.Editor editor = preference.edit();
                    editor.putString("userPassword", userDto.getUserLogin().getUserPassword());
                    editor.putString("userName", userDto.getUserInfo().getUserName());
                    editor.putString("userPhone", userDto.getUserInfo().getUserPhone());
                    editor.putString("userImgUrl", userDto.getUserInfo().getUserImgUrl());
                    editor.putString("userId", userDto.getUserInfo().getUserId());
                    editor.putString("userBirthday", userDto.getUserInfo().getUserBirthday());
                    editor.putString("userReallyName", userDto.getUserInfo().getUserReallyName());
                    editor.putString("userSex", userDto.getUserInfo().getUserSex());
                    editor.putString("userJoinDate", userDto.getUserInfo().getUserJoinDate());
                    StaticValues.USER_ID=userDto.getUserInfo().getUserId();
                    editor.commit();//提交数据
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    LoginActivity.this.finish();
                }
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

    private String[] premissins = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS};

    public void verifyPermission() {
        boolean needpremission = false;
        for (String premission : premissins) {
            if (ContextCompat.checkSelfPermission(LoginActivity.this, premission) != PackageManager.PERMISSION_GRANTED) {
                needpremission = true;
                break;
            }
        }
        if (!needpremission) {
            phone.setText(getPhone());
        } else {
            ActivityCompat.requestPermissions(LoginActivity.this, premissins, CALL_PHONE_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    phone.setText(getPhone());
                } else {
                    Toast.makeText(this, "未能拿到权限", Toast.LENGTH_SHORT).show();
                    this.finish();
                }
                break;
            default:
        }
    }

    public String getPhone() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String te1 = tm.getLine1Number();//获取本机号码
        if (te1.length() > 11) {
            te1 = te1.substring(te1.length() - 11);
        }
        return te1;
    }

    public void register() {

        if (phone.getText().toString().equals("")) {
            showToast("请输入手机号");
            return;
        } else if (password.getText().toString().equals("")) {
            showToast("请输入密码");
            return;
        }
        hgLoadDialog = new HGLoadDialog.Builder(this).setHint("正在注册...").setOutside(false).create();
        hgLoadDialog.show();
        RequestBody requestBody = new FormBody.Builder()
                .add("password", password.getText().toString())
                .add("userPhone", phone.getText().toString())
                .build();
        httpHelper.post(StaticValues.HOST_URL + "/user/register", requestBody, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        hgLoadDialog.cancel();
                        showToast("连接失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseInfoDto responseInfoDto = JSONObject.parseObject(response.body().string(), ResponseInfoDto.class);
                if (responseInfoDto.getCode() != 100) {
                    System.out.println(responseInfoDto.toString());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            hgLoadDialog.cancel();
                            showToast(responseInfoDto.getMsg());
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            hgLoadDialog.cancel();
                            registerLayout.setVisibility(View.GONE);
                            loginLayout.setVisibility(View.VISIBLE);
                            phone.setEnabled(true);
                            showToast("注册完成");
                        }
                    });

                }
            }
        });
    }
}
