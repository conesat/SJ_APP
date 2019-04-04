package com.hg.sj_app.view;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.hg.sj_app.R;
import com.hg.sj_app.activity.FileManagerActivity;
import com.hg.sj_app.activity.RemoteActivity;

public class ViewHome extends LinearLayout implements View.OnClickListener {

    private View view;

    private LinearLayout fileManager;
    private LinearLayout remote;


    public ViewHome(Context context) {
        super(context);
        view = inflate(getContext(), R.layout.tab_home_layout, this);
        initView();
    }

    private void initView() {
        fileManager = view.findViewById(R.id.view_home_file_manager);
        remote=view.findViewById(R.id.view_home_remote);
        remote.setOnClickListener(this::onClick);
        fileManager.setOnClickListener(this::onClick);
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
        }
    }
}
