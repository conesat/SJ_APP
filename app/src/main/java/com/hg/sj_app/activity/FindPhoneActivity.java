package com.hg.sj_app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.hg.sj_app.R;

public class FindPhoneActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_phone);
        initView();
    }

    private void initView() {
        back = findViewById(R.id.find_phone_back);
        back.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.find_phone_back:
                this.finish();
                break;
        }

    }
}
