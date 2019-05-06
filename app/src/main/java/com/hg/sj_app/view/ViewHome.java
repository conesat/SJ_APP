package com.hg.sj_app.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.hg.sj_app.R;
import com.hg.sj_app.activity.FaceActivity;
import com.hg.sj_app.activity.FileManagerActivity;
import com.hg.sj_app.activity.FindPhoneActivity;
import com.hg.sj_app.activity.RemoteActivity;
import com.hg.sj_app.activity.ShareActivity;

public class ViewHome extends LinearLayout implements View.OnClickListener {

    private View view;

    private LinearLayout fileManager;
    private LinearLayout remote;
    private LinearLayout findPhone;
    private LinearLayout face;
    private LinearLayout share;


    public ViewHome(Context context) {
        super(context);
        view = inflate(getContext(), R.layout.tab_home_layout, this);
        initView();
    }

    private void initView() {
        fileManager = view.findViewById(R.id.view_home_file_manager);
        remote=view.findViewById(R.id.view_home_remote);
        findPhone=view.findViewById(R.id.view_home_find_phone);
        face=view.findViewById(R.id.view_home_face);
        share=view.findViewById(R.id.view_home_share);

        remote.setOnClickListener(this::onClick);
        fileManager.setOnClickListener(this::onClick);
        findPhone.setOnClickListener(this::onClick);
        face.setOnClickListener(this::onClick);
        share.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_home_file_manager:
                getContext().startActivity(new Intent(getContext(), FileManagerActivity.class));
                break;
            case R.id.view_home_remote:
                getContext().startActivity(new Intent(getContext(), RemoteActivity.class));
                break;
            case R.id.view_home_find_phone:
                getContext().startActivity(new Intent(getContext(), FindPhoneActivity.class));
                break;
            case R.id.view_home_face:
                getContext().startActivity(new Intent(getContext(), FaceActivity.class));
                break;
            case R.id.view_home_share:
                getContext().startActivity(new Intent(getContext(), ShareActivity.class));
                break;
        }
    }
}
