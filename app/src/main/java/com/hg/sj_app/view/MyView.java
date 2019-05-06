package com.hg.sj_app.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hg.sj_app.LoginActivity;
import com.hg.sj_app.R;
import com.hg.sj_app.activity.DownloadActivity;
import com.hg.sj_app.activity.InfoEditActivity;
import com.hg.sj_app.values.StaticValues;
import com.hg.ui.animator.HGSelectViewAnimation;
import com.hg.ui.view.CircularImageView;
import com.hg.ui.view.RoundLinearlayout;
import com.squareup.picasso.Picasso;

import static android.content.Context.MODE_PRIVATE;

public class MyView extends LinearLayout implements View.OnClickListener {
    private SharedPreferences preference;

    private TextView titleMyTxt;//顶部标题
    private TextView myTxt;//顶部下边的标题  滑动后悔隐藏
    private ScrollView scrollView;//滑动部分
    private ImageView loginOut;//退出按钮

    //获取分辨率 处理滑动效果
    private DisplayMetrics dm;
    private Rect rect;
    private int[] location = new int[2];

    private CircularImageView userImg;//用户头像
    private TextView userName;//用户名
    private TextView userPhone;//用户手机
    private RoundLinearlayout editInfo;//编辑按钮

    private RoundLinearlayout downloadManager;


    public MyView(Context context) {
        super(context);
        dm = getResources().getDisplayMetrics();
        rect = new Rect(0, 0, dm.widthPixels, dm.heightPixels);
        initView();
        initSetting();
    }

    /**
     * 初始化界面
     */
    private void initView() {
        preference =getContext().getSharedPreferences("user", MODE_PRIVATE);
        View view = inflate(getContext(), R.layout.tab_my_layout, this);
        scrollView = view.findViewById(R.id.my_scroll_view);
        titleMyTxt = view.findViewById(R.id.my_title_my_text);
        myTxt = view.findViewById(R.id.my_my_text);
        loginOut = findViewById(R.id.my_title_login_out);


        userImg=view.findViewById(R.id.my_user_img);
        userName=view.findViewById(R.id.my_user_name);
        userPhone=view.findViewById(R.id.my_user_phone);
        editInfo=view.findViewById(R.id.my_user_edit);

        downloadManager=findViewById(R.id.my_download_manager);


        loginOut.setOnClickListener(this);
        editInfo.setOnClickListener(this);
        downloadManager.setOnClickListener(this::onClick);

        userName.setText(preference.getString("userName", ""));
        userPhone.setText(preference.getString("userPhone", ""));
        String imgUrl=preference.getString("userImgUrl", "");


        if (imgUrl!=null&&!imgUrl.equals("")){
            Picasso.with(getContext()).load(StaticValues.HOST_URL+"userFile/getFile?path="+imgUrl).into(userImg);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener(new OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    myTxt.getLocationInWindow(location);
                    if (!myTxt.getLocalVisibleRect(rect)) {
                        myTxt.setVisibility(INVISIBLE);
                        titleMyTxt.setVisibility(VISIBLE);
                    } else {
                        myTxt.setVisibility(VISIBLE);
                        titleMyTxt.setVisibility(INVISIBLE);
                    }
                }
            });
        }

    }

    //设置部分
    private ImageView msgSettingImg;
    private LinearLayout msgSettingLayou;

    private ImageView aboutImg;
    private LinearLayout aboutLayout;

    public void initSetting() {
        msgSettingImg = findViewById(R.id.my_setting_msg_setting_img);
        msgSettingLayou = findViewById(R.id.my_setting_msg_setting_layout);

        aboutImg = findViewById(R.id.my_about_img);
        aboutLayout = findViewById(R.id.my_about_layout);

        new HGSelectViewAnimation(msgSettingImg,msgSettingLayou,msgSettingImg);//下拉工具 消息设置面板

        new HGSelectViewAnimation(aboutImg,aboutLayout,aboutImg);//下拉工具 关于面板

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_user_edit:
                getContext().startActivity(new Intent(getContext(), InfoEditActivity.class));
                break;
            case R.id.my_title_login_out:
                SharedPreferences.Editor editor=preference.edit();
                editor.putString("userPhone", "");
                editor.commit();//提交数据
                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                ((Activity)getContext()).finish();
                break;
            case R.id.my_download_manager:
                getContext().startActivity(new Intent(getContext(), DownloadActivity.class));
                break;

        }
    }

    public void resetName(){
        userName.setText(preference.getString("userName", ""));
        String imgUrl=preference.getString("userImgUrl", "");
        if (imgUrl!=null&&!imgUrl.equals("")){
            Picasso.with(getContext()).load(StaticValues.HOST_URL+"/userFile/getFile?path="+imgUrl).into(userImg);
        }
    }
}
