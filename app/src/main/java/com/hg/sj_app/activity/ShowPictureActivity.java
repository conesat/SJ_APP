package com.hg.sj_app.activity;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.hg.sj_app.R;
import com.hg.sj_app.util.EncryptionFile;
import com.hg.sj_app.view.ImageViewViewer;
import com.hg.ui.dialog.HGLoadDialog;

public class ShowPictureActivity extends Activity {
    private ImageViewViewer show;
    private String path;
    private Handler handler;
    private HGLoadDialog hgLoadDialog;

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
        setContentView(R.layout.activity_picture_view);
        path = getIntent().getStringExtra("path");
        initView();
    }

    private void initView() {
        show = (ImageViewViewer) findViewById(R.id.iv_show);
        handler = new Handler();

        hgLoadDialog = new HGLoadDialog.Builder(this).setHint("读取中...").setOutside(false).create();
        hgLoadDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                EncryptionFile encryptionFile = new EncryptionFile(ShowPictureActivity.this);
                byte[] bytes = encryptionFile.decryptFile(path);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        hgLoadDialog.dismiss();
                        if (bytes == null) {
                            showToast("无权查看");
                        } else {
                            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            show.setImageBitmap(bm);
                        }
                    }
                });
            }
        }).start();


    }

    private

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
