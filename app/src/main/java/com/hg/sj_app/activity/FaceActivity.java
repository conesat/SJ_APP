package com.hg.sj_app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.FaceEngine;
import com.hg.sj_app.R;
import com.hg.sj_app.common.Constants;
import com.hg.sj_app.util.ConfigUtil;
import com.hg.sj_app.values.StaticValues;
import com.hg.ui.view.RoundLinearlayout;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FaceActivity extends AppCompatActivity implements View.OnClickListener {
    private Toast toast = null;
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };
    /**
     * 请求选择本地图片文件的请求码
     */
    private static final int ACTION_CHOOSE_IMAGE = 0x201;

    private ImageView back;

    private RoundLinearlayout facePiture;
    private RoundLinearlayout faceScanning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face);
        initView();
    }

    private void initView() {
        back = findViewById(R.id.face_back);
        facePiture = findViewById(R.id.face_picture);
        faceScanning = findViewById(R.id.face_scanning);
        back.setOnClickListener(this::onClick);
        facePiture.setOnClickListener(this::onClick);
        faceScanning.setOnClickListener(this::onClick);

        //设置视频模式下的人脸优先检测方向
        ConfigUtil.setFtOrient(FaceActivity.this, FaceEngine.ASF_OP_0_ONLY);
        activeEngine();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.face_back:
                this.finish();
                break;
            case R.id.face_picture:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, ACTION_CHOOSE_IMAGE);
                break;
            case R.id.face_scanning:
                startActivity(new Intent(FaceActivity.this, PreviewActivity.class));
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            boolean isAllGranted = true;
            for (int grantResult : grantResults) {
                isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
            }
            if (!isAllGranted) {
                showToast(getString(R.string.permission_denied));
            }
        }
    }

    private boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    private void showToast(String s) {
        if (toast == null) {
            toast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(s);
            toast.show();
        }
    }


    /**
     * 激活引擎
     */
    public void activeEngine() {
        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
            return;
        }

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                FaceEngine faceEngine = new FaceEngine();
                int activeCode = faceEngine.active(FaceActivity.this, Constants.APP_ID, Constants.SDK_KEY);
                emitter.onNext(activeCode);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer activeCode) {
                        if (activeCode == ErrorInfo.MOK) {
                            //  showToast(getString(R.string.active_success));
                        } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                            //   showToast(getString(R.string.already_activated));
                        } else {
                            showToast(getString(R.string.active_failed, activeCode));
                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_CHOOSE_IMAGE) {
            if (data == null || data.getData() == null) {
                Toast.makeText(this, R.string.get_picture_failed, Toast.LENGTH_SHORT).show();
                return;
            }
            if (data.getData() == null) {
                Toast.makeText(this, R.string.get_picture_failed, Toast.LENGTH_SHORT).show();
                return;
            } else {
                StaticValues.FACE_URI = data.getData();
                startActivity(new Intent(this, SingleImageActivity.class));
            }
        }
    }

    public void addFaceFind(View view){
        startActivity(new Intent(FaceActivity.this,AddFaceFindActivity.class));
    }
}
