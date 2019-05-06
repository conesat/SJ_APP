package com.hg.sj_app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hg.sj_app.R;

public class FileDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);
    }

    public void back(View view){
        this.finish();
    }
}
