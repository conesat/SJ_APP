package com.hg.sj_app.view;

import android.content.Context;
import android.content.Intent;
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
import com.hg.ui.animator.HGSelectViewAnimation;

public class MyView extends LinearLayout implements View.OnClickListener {
    private TextView titleMyTxt;
    private TextView myTxt;
    private ScrollView scrollView;
    private ImageView loginOut;

    private DisplayMetrics dm;
    private Rect rect;
    private int[] location = new int[2];

    public MyView(Context context) {
        super(context);
        dm = getResources().getDisplayMetrics();
        rect = new Rect(0, 0, dm.widthPixels, dm.heightPixels);
        initView();
        initSetting();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.tab_my_layout, this);
        scrollView = view.findViewById(R.id.my_scroll_view);
        titleMyTxt = view.findViewById(R.id.my_title_my_text);
        myTxt = view.findViewById(R.id.my_my_text);
        loginOut = findViewById(R.id.my_title_login_out);

        loginOut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

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

    //setting
    private ImageView msgSettingImg;
    private LinearLayout msgSettingLayou;

    private ImageView aboutImg;
    private LinearLayout aboutLayout;

    public void initSetting() {
        msgSettingImg = findViewById(R.id.my_setting_msg_setting_img);
        msgSettingLayou = findViewById(R.id.my_setting_msg_setting_layout);

        aboutImg = findViewById(R.id.my_about_img);
        aboutLayout = findViewById(R.id.my_about_layout);

        new HGSelectViewAnimation(msgSettingImg,msgSettingLayou,msgSettingImg);

        new HGSelectViewAnimation(aboutImg,aboutLayout,aboutImg);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
