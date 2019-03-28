package com.hg.sj_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
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
import com.hg.sj_app.values.StaticValues;
import com.hg.ui.dialog.HGLoadDialog;
import com.hg.ui.view.RoundLinearlayout;

import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Handler handler;

    private LinearLayout loginLayout;
    private LinearLayout registerLayout;

    private LinearLayout layout;
    private ImageView bgView;

    //登录
    private RoundLinearlayout login;
    private RoundLinearlayout register;

    //注册
    private RoundLinearlayout registerSubmit;
    private RoundLinearlayout cencleRegister;

    private HttpHelper httpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            //   getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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
        SharedPreferences preference = getSharedPreferences("user", MODE_PRIVATE);
        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 10) {
            bgView.setImageResource(R.drawable.good_night_img);
        } else if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 17) {
            ((TextView) findViewById(R.id.loding_big_text)).setText("晚上好");
            ((TextView) findViewById(R.id.loding_min_text)).setText("熬过了黑夜就是黎明");
            bgView.setImageResource(R.drawable.good_night_img);
        } else {

        }
        Handler handler = new Handler();
        if (preference.getString("userName", "").equals("")) {
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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_layout_register:
                loginLayout.setVisibility(View.GONE);
                registerLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.register_layout_cancel:
                registerLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.login_layout_login:
                login();
                break;
        }
    }


    public void login() {
        String phone = ((TextView) findViewById(R.id.login_user_phone)).getText().toString();
        String password = ((TextView) findViewById(R.id.login_user_password)).getText().toString();
        if (phone.equals("")) {
            showToast("请输入手机号");
            return;
        } else if (password.equals("")) {
            showToast("请输入密码");
            return;
        } else {
            String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if (phone.length() != 11 || !isMatch) {
                showToast("请填入正确的手机号");
                return;
            }
        }

        HGLoadDialog hgLoadDialog = new HGLoadDialog.Builder(this).setHint("正在登陆...").setOutside(false).create();
        hgLoadDialog.show();

        RequestBody requestBody = new FormBody.Builder()
                .add("userPassword", password)
                .add("userPhone", phone)
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

}
