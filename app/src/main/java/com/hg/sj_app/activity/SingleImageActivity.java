package com.hg.sj_app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.ParcelableSpan;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.ErrorInfo;
import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.VersionInfo;
import com.hg.sj_app.R;
import com.hg.sj_app.helper.HttpHelper;
import com.hg.sj_app.util.FileUtilcll;
import com.hg.sj_app.util.ImageUtil;
import com.hg.sj_app.values.StaticValues;
import com.hg.ui.animator.HGSelectViewAnimation;
import com.hg.ui.dialog.HGButtomDialog;
import com.hg.ui.dialog.HGLoadDialog;
import com.hg.ui.listener.HGBottomDialogOnClickListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SingleImageActivity extends AppCompatActivity {
    private static final String TAG = "SingleImageActivity";
    private ImageView ivShow;
    private TextView tvNotice;
    private FaceEngine faceEngine;
    private int faceEngineCode = -1;
    private HttpHelper httpHelper;
    private Handler handler;
    private HGLoadDialog hgLoadDialog;


    HGButtomDialog userImgDialog=null;

    private File chooseFile;

    private boolean needSearch = true;
    /**
     * 请求权限的请求码
     */
    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    /**
     * 请求选择本地图片文件的请求码
     */
    private static final int ACTION_CHOOSE_IMAGE = 0x201;
    /**
     * 提示对话框
     */
    private AlertDialog progressDialog;
    /**
     * 被处理的图片
     */
    private Bitmap mBitmap = null;

    /**
     * 所需的所有权限信息
     */
    private static String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_process);
        initView();

        /**
         * 在选择图片的时候，在android 7.0及以上通过FileProvider获取Uri，不需要文件权限
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            List<String> permissionList = new ArrayList<>(Arrays.asList(NEEDED_PERMISSIONS));
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            NEEDED_PERMISSIONS = permissionList.toArray(new String[0]);
        }

        if (!checkPermissions(NEEDED_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
        } else {
            initEngine();
        }

        if (StaticValues.FACE_URI != null) {
            chooseFile = FileUtilcll.uriToFile(StaticValues.FACE_URI, this);
            mBitmap = ImageUtil.getBitmapFromUri(StaticValues.FACE_URI, this);
            if (mBitmap == null) {
                Toast.makeText(this, R.string.get_picture_failed, Toast.LENGTH_SHORT).show();
                return;
            }
            ivShow.setImageBitmap(mBitmap);
            needSearch = true;
            process();
            StaticValues.FACE_URI = null;
        } else if (StaticValues.FACE_SCANNING_BITMAP != null && StaticValues.FACE_SCANNING_JSON != null) {
            ivShow.setImageBitmap(StaticValues.FACE_SCANNING_BITMAP);
            mBitmap = StaticValues.FACE_SCANNING_BITMAP;
            needSearch = false;
            process();
        }

        handler = new Handler();
        httpHelper = new HttpHelper(this, "");
    }

    private void initEngine() {
        faceEngine = new FaceEngine();
        faceEngineCode = faceEngine.init(this, FaceEngine.ASF_DETECT_MODE_IMAGE, FaceEngine.ASF_OP_0_HIGHER_EXT,
                16, 10, FaceEngine.ASF_FACE_RECOGNITION | FaceEngine.ASF_FACE_DETECT | FaceEngine.ASF_AGE | FaceEngine.ASF_GENDER | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_LIVENESS);
        VersionInfo versionInfo = new VersionInfo();
        faceEngine.getVersion(versionInfo);
        Log.i(TAG, "initEngine: init: " + faceEngineCode + "  version:" + versionInfo);

        if (faceEngineCode != ErrorInfo.MOK) {
            Toast.makeText(this, getString(R.string.init_failed, faceEngineCode), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 销毁引擎
     */
    private void unInitEngine() {
        if (faceEngine != null) {
            faceEngineCode = faceEngine.unInit();
            faceEngine = null;
            Log.i(TAG, "unInitEngine: " + faceEngineCode);
        }
    }

    @Override
    protected void onDestroy() {
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
        }
        mBitmap = null;

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;

        unInitEngine();
        super.onDestroy();
    }

    private void initView() {
        tvNotice = findViewById(R.id.tv_notice);
        ivShow = findViewById(R.id.iv_show);
        ivShow.setImageResource(R.mipmap.faces);
        progressDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.processing)
                .setView(new ProgressBar(this))
                .create();
        List<String> menus = new ArrayList<>();
        menus.add("选取图片");
        menus.add("扫一扫");
         userImgDialog = new HGButtomDialog(this, menus);
        userImgDialog.setHgBottomDialogOnClickListener(new HGBottomDialogOnClickListener() {
            @Override
            public void onClick(View v, int i) {
                userImgDialog.dismiss();
                if (i == 0) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, ACTION_CHOOSE_IMAGE);
                }else {
                    startActivity(new Intent(SingleImageActivity.this,PreviewActivity.class));
                }
            }
        });
    }

    /**
     * 按钮点击响应事件
     */
    public void process() {

        if (progressDialog == null || progressDialog.isShowing()) {
            return;
        }
        progressDialog.show();
        //图像转化操作和部分引擎调用比较耗时，建议放子线程操作
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                processImage();
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    /**
     * 主要操作逻辑部分
     */
    public void processImage() {
        /**
         * 1.准备操作（校验，显示，获取BGR）
         */
        if (mBitmap == null) {
            mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.faces);
        }
        Bitmap bitmap = mBitmap.copy(Bitmap.Config.ARGB_8888, true);

        final SpannableStringBuilder notificationSpannableStringBuilder = new SpannableStringBuilder();
        if (faceEngineCode != ErrorInfo.MOK) {
            addNotificationInfo(notificationSpannableStringBuilder, null, " 初始化失败，请联系客服!");
            showNotificationAndFinish(notificationSpannableStringBuilder);
            return;
        }
        if (bitmap == null) {
            addNotificationInfo(notificationSpannableStringBuilder, null, " 选取图片为空!");
            showNotificationAndFinish(notificationSpannableStringBuilder);
            return;
        }
        if (faceEngine == null) {
            addNotificationInfo(notificationSpannableStringBuilder, null, " 初始化失败，请联系客服!");
            showNotificationAndFinish(notificationSpannableStringBuilder);
            return;
        }

        bitmap = ImageUtil.alignBitmapForBgr24(bitmap);


        if (bitmap == null) {
            addNotificationInfo(notificationSpannableStringBuilder, null, " 选取图片为空!");
            showNotificationAndFinish(notificationSpannableStringBuilder);
            return;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        final Bitmap finalBitmap = bitmap;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivShow.setImageBitmap(finalBitmap);
            }
        });

        //bitmap转bgr
        byte[] bgr24 = ImageUtil.bitmapToBgr(bitmap);

        addNotificationInfo(notificationSpannableStringBuilder, null, "宽度 " + width + "\n高度 " + height + "\n");

        if (bgr24 == null) {
            addNotificationInfo(notificationSpannableStringBuilder, new ForegroundColorSpan(Color.RED), "无法转换该格式图片!\n");
            showNotificationAndFinish(notificationSpannableStringBuilder);
            return;
        }
        List<FaceInfo> faceInfoList = new ArrayList<>();


        /**
         * 2.成功获取到了BGR24 数据，开始人脸检测
         */
        long fdStartTime = System.currentTimeMillis();
        int detectCode = faceEngine.detectFaces(bgr24, width, height, FaceEngine.CP_PAF_BGR24, faceInfoList);
        if (detectCode == ErrorInfo.MOK) {
//            Log.i(TAG, "processImage: fd costTime = " + (System.currentTimeMillis() - fdStartTime));
        }

        //绘制bitmap
        Bitmap bitmapForDraw = bitmap.copy(Bitmap.Config.RGB_565, true);
        Canvas canvas = new Canvas(bitmapForDraw);
        Paint paint = new Paint();
        addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "\n检测结果:\n");
        addNotificationInfo(notificationSpannableStringBuilder, null, "代码 :", String.valueOf(detectCode), "  \n人脸数", String.valueOf(faceInfoList.size()), "\n");
        /**
         * 3.若检测结果人脸数量大于0，则在bitmap上绘制人脸框并且重新显示到ImageView，若人脸数量为0，则无法进行下一步操作，操作结束
         */
        if (faceInfoList.size() > 0) {
            addNotificationInfo(notificationSpannableStringBuilder, null, "人脸矩阵:\n");
            paint.setAntiAlias(true);
            paint.setStrokeWidth(5);
            paint.setColor(Color.YELLOW);
            for (int i = 0; i < faceInfoList.size(); i++) {
                //绘制人脸框
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(faceInfoList.get(i).getRect(), paint);
                //绘制人脸序号
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                int textSize = faceInfoList.get(i).getRect().width() / 2;
                paint.setTextSize(textSize);

                canvas.drawText(String.valueOf(i), faceInfoList.get(i).getRect().left, faceInfoList.get(i).getRect().top, paint);
                addNotificationInfo(notificationSpannableStringBuilder, null, "第 ", String.valueOf(i), " 个人:", faceInfoList.get(i).toString(), "\n");
            }
            //显示
            final Bitmap finalBitmapForDraw = bitmapForDraw;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivShow.setImageBitmap(finalBitmapForDraw);
                }
            });
        } else {
            addNotificationInfo(notificationSpannableStringBuilder, null, "无法继续检测!");
            showNotificationAndFinish(notificationSpannableStringBuilder);
            return;
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n");


        /**
         * 4.上一步已获取到人脸位置和角度信息，传入给process函数，进行年龄、性别、三维角度检测
         */

        long processStartTime = System.currentTimeMillis();
        int faceProcessCode = faceEngine.process(bgr24, width, height, FaceEngine.CP_PAF_BGR24, faceInfoList, FaceEngine.ASF_AGE | FaceEngine.ASF_GENDER | FaceEngine.ASF_FACE3DANGLE | FaceEngine.ASF_LIVENESS);

        if (faceProcessCode != ErrorInfo.MOK) {
            addNotificationInfo(notificationSpannableStringBuilder, new ForegroundColorSpan(Color.RED), "检测失败，代码为 ", String.valueOf(faceProcessCode), "\n");
        } else {
//            Log.i(TAG, "processImage: process costTime = " + (System.currentTimeMillis() - processStartTime));
        }
        //年龄信息结果
        List<AgeInfo> ageInfoList = new ArrayList<>();
        //性别信息结果
        List<GenderInfo> genderInfoList = new ArrayList<>();
        //人脸三维角度结果
        List<Face3DAngle> face3DAngleList = new ArrayList<>();
        //活体检测结果
        List<LivenessInfo> livenessInfoList = new ArrayList<>();
        //获取年龄、性别、三维角度、活体结果
        int ageCode = faceEngine.getAge(ageInfoList);
        int genderCode = faceEngine.getGender(genderInfoList);
        int face3DAngleCode = faceEngine.getFace3DAngle(face3DAngleList);
        int livenessCode = faceEngine.getLiveness(livenessInfoList);

        if ((ageCode | genderCode | face3DAngleCode | livenessCode) != ErrorInfo.MOK) {
            addNotificationInfo(notificationSpannableStringBuilder, null, "年龄、性别、3d检不合法!,代码为:",
                    String.valueOf(ageCode), " , ", String.valueOf(genderCode), " , ", String.valueOf(face3DAngleCode));
            showNotificationAndFinish(notificationSpannableStringBuilder);
            return;
        }
        /**
         * 5.年龄、性别、三维角度已获取成功，添加信息到提示文字中
         */
        //年龄数据
        if (ageInfoList.size() > 0) {
            addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "年龄:\n");
        }
        for (int i = 0; i < ageInfoList.size(); i++) {
            addNotificationInfo(notificationSpannableStringBuilder, null, "第 ", String.valueOf(i), " 个人:", String.valueOf(ageInfoList.get(i).getAge()), "\n");
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n");

        //性别数据
        if (genderInfoList.size() > 0) {
            addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "性别:\n");
        }
        for (int i = 0; i < genderInfoList.size(); i++) {
            addNotificationInfo(notificationSpannableStringBuilder, null, "第 ", String.valueOf(i), " 个人:"
                    , genderInfoList.get(i).getGender() == GenderInfo.MALE ?
                            "男" : (genderInfoList.get(i).getGender() == GenderInfo.FEMALE ? "女" : "无法识别"), "\n");
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n");


        //人脸三维角度数据
        if (face3DAngleList.size() > 0) {
            addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "人脸3d信息:\n");
            for (int i = 0; i < face3DAngleList.size(); i++) {
                addNotificationInfo(notificationSpannableStringBuilder, null, "第 ", String.valueOf(i), " 个人:", face3DAngleList.get(i).toString(), "\n");
            }
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n");

        //活体检测数据
        if (livenessInfoList.size() > 0) {
            addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "是否活体:\n");
            for (int i = 0; i < livenessInfoList.size(); i++) {
                String liveness = null;
                switch (livenessInfoList.get(i).getLiveness()) {
                    case LivenessInfo.ALIVE:
                        liveness = "活体";
                        break;
                    case LivenessInfo.NOT_ALIVE:
                        liveness = "非活体";
                        break;
                    case LivenessInfo.UNKNOWN:
                        liveness = "无法检测";
                        break;
                    case LivenessInfo.FACE_NUM_MORE_THAN_ONE:
                        liveness = "多张人脸，不做检测";
                        break;
                    default:
                        liveness = "未知";
                        break;
                }
                addNotificationInfo(notificationSpannableStringBuilder, null, "第 ", String.valueOf(i), " 个人:", liveness, "\n");
            }
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n");

        /**
         * 6.最后将图片内的所有人脸进行一一比对并添加到提示文字中
         */
        if (faceInfoList.size() > 0) {

            FaceFeature[] faceFeatures = new FaceFeature[faceInfoList.size()];
            int[] extractFaceFeatureCodes = new int[faceInfoList.size()];

            addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "人脸特征提取:\n");
            for (int i = 0; i < faceInfoList.size(); i++) {
                faceFeatures[i] = new FaceFeature();
                //从图片解析出人脸特征数据
                long frStartTime = System.currentTimeMillis();
                extractFaceFeatureCodes[i] = faceEngine.extractFaceFeature(bgr24, width, height, FaceEngine.CP_PAF_BGR24, faceInfoList.get(i), faceFeatures[i]);

                if (extractFaceFeatureCodes[i] != ErrorInfo.MOK) {
                    addNotificationInfo(notificationSpannableStringBuilder, null, "第 ", String.valueOf(i), " 个人脸特征",
                            " 检测失败，代码为 ", String.valueOf(extractFaceFeatureCodes[i]), "\n");
                } else {
//                    Log.i(TAG, "processImage: fr costTime = " + (System.currentTimeMillis() - frStartTime));
                    addNotificationInfo(notificationSpannableStringBuilder, null, "第 ", String.valueOf(i), " 个人脸特征",
                            " 检测成功\n");
                }
            }
            addNotificationInfo(notificationSpannableStringBuilder, null, "\n");

            //人脸特征的数量大于2，将所有特征进行比较
            if (faceFeatures.length >= 2) {

                addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD), "相似人脸检测:\n");

                for (int i = 0; i < faceFeatures.length; i++) {
                    for (int j = i + 1; j < faceFeatures.length; j++) {
                        addNotificationInfo(notificationSpannableStringBuilder, new StyleSpan(Typeface.BOLD_ITALIC), "第 ", String.valueOf(i), " 个与第 "
                                , String.valueOf(j), " 个:\n");
                        //若其中一个特征提取失败，则不进行比对
                        boolean canCompare = true;
                        if (extractFaceFeatureCodes[i] != 0) {
                            addNotificationInfo(notificationSpannableStringBuilder, null, "第 ", String.valueOf(i), " 个人脸检测失败, 无法对比!\n");
                            canCompare = false;
                        }
                        if (extractFaceFeatureCodes[j] != 0) {
                            addNotificationInfo(notificationSpannableStringBuilder, null, "第 ", String.valueOf(j), " 个人脸检测失败, 无法对比!\n");
                            canCompare = false;
                        }
                        if (!canCompare) {
                            continue;
                        }

                        FaceSimilar matching = new FaceSimilar();
                        //比对两个人脸特征获取相似度信息
                        faceEngine.compareFaceFeature(faceFeatures[i], faceFeatures[j], matching);
                        //新增相似度比对结果信息
                        addNotificationInfo(notificationSpannableStringBuilder, null, "第 ", String.valueOf(i), " 与 第 ",
                                String.valueOf(j), " 相似度为 :", String.valueOf(matching.getScore()), "\n");
                    }
                }
            }
        }

        showNotificationAndFinish(notificationSpannableStringBuilder);

    }

    /**
     * 展示提示信息并且关闭提示框
     *
     * @param stringBuilder 带格式的提示文字
     */
    private void showNotificationAndFinish(final SpannableStringBuilder stringBuilder) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (tvNotice != null) {
                    findViewById(R.id.face_image_info_layout).setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    tvNotice.setText(stringBuilder);
                    new HGSelectViewAnimation(findViewById(R.id.face_image_info_more), findViewById(R.id.face_image_info_layout), findViewById(R.id.face_image_info_more));//下拉工具 基本信息
                    findFace();
                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });

    }

    /**
     * 追加提示信息
     *
     * @param stringBuilder 提示的字符串的存放对象
     * @param styleSpan     添加的字符串的格式
     * @param strings       字符串数组
     */
    private void addNotificationInfo(SpannableStringBuilder stringBuilder, ParcelableSpan styleSpan, String... strings) {
        if (stringBuilder == null || strings == null || strings.length == 0) {
            return;
        }
        int startLength = stringBuilder.length();
        for (String string : strings) {
            stringBuilder.append(string);
        }
        int endLength = stringBuilder.length();
        if (styleSpan != null) {
            stringBuilder.setSpan(styleSpan, startLength, endLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * 权限检测
     *
     * @param neededPermissions 所需的所有权限
     * @return 是否检测通过
     */
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

    /**
     * 从本地选择文件
     *
     * @param view
     */
    public void chooseLocalImage(View view) {
        userImgDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_CHOOSE_IMAGE) {
            if (data == null || data.getData() == null) {
                Toast.makeText(this, R.string.get_picture_failed, Toast.LENGTH_SHORT).show();
                return;
            }
            findViewById(R.id.find_none).setVisibility(View.VISIBLE);
            findViewById(R.id.find_info).setVisibility(View.GONE);
            chooseFile = FileUtilcll.uriToFile(data.getData(), this);
            mBitmap = ImageUtil.getBitmapFromUri(data.getData(), this);
            if (mBitmap == null) {
                Toast.makeText(this, R.string.get_picture_failed, Toast.LENGTH_SHORT).show();
                return;
            }
            needSearch = true;
            ivShow.setImageBitmap(mBitmap);
            process();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            boolean isAllGranted = true;
            for (int grantResult : grantResults) {
                isAllGranted &= grantResult == PackageManager.PERMISSION_GRANTED;
            }
            if (isAllGranted) {
                initEngine();
            } else {
                Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void findFace() {
        if (needSearch) {
            hgLoadDialog = new HGLoadDialog.Builder(this).setHint("请稍等...").setOutside(false).create();
            hgLoadDialog.show();

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", chooseFile.getName(),
                            RequestBody.create(MediaType.parse("multipart/form-data"), chooseFile))
                    .addFormDataPart("groupId", "101")
                    .build();

            httpHelper.post(StaticValues.FACE_SERVICE_URL + "/faceSearch", requestBody, new Callback() {
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
                    JSONObject jsonObject = JSONObject.parseObject(response.body().string());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            hgLoadDialog.cancel();
                            setText(jsonObject);
                        }
                    });
                }
            });
        } else {
            setText(StaticValues.FACE_SCANNING_JSON);
        }

    }

    public void setText(JSONObject jsonObject) {
        if (jsonObject.getString("code").equals("0")) {
            JSONObject finalJsonObject = JSONObject.parseObject(jsonObject.getString("data"));


            findViewById(R.id.find_none).setVisibility(View.GONE);
            findViewById(R.id.find_info).setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.find_similarValue)).setText("相似度：" + finalJsonObject.getString("similarValue"));

            String imgUrl = StaticValues.FACE_SERVICE_URL + "/getFile?path=" + finalJsonObject.getJSONObject("faceInfo").getString("imageUrl");
            Picasso.with(SingleImageActivity.this).load(imgUrl).into(((ImageView) findViewById(R.id.find_img)));

            String sendTime = finalJsonObject.getJSONObject("faceInfo").getString("createTime");
            String birthday = finalJsonObject.getJSONObject("faceInfo").getString("birthday");
            if (sendTime != null) {
                ((TextView) findViewById(R.id.find_creat_time)).setText("发布时间：" + sendTime.split("T")[0]);
            }
            if (birthday != null) {
                ((TextView) findViewById(R.id.find_birthday)).setText("生日：" + birthday.split("T")[0]);
            }
            finalJsonObject = JSONObject.parseObject(finalJsonObject.getString("findInfo"));

            ((TextView) findViewById(R.id.find_type)).setText("类型：" + finalJsonObject.getString("type"));
            ((TextView) findViewById(R.id.find_name)).setText("姓名：" + finalJsonObject.getString("name"));
            ((TextView) findViewById(R.id.find_idcard)).setText("身份证：" + finalJsonObject.getString("idcard"));

            ((TextView) findViewById(R.id.find_sex)).setText("性别：" + finalJsonObject.getString("sex"));
            ((TextView) findViewById(R.id.find_pcc)).setText("籍贯：" + finalJsonObject.getString("city"));

            ((TextView) findViewById(R.id.find_sender)).setText("发布者：" + finalJsonObject.getString("sender"));
            ((TextView) findViewById(R.id.find_send_city)).setText("发布城市：" + finalJsonObject.getString("sendCity"));
            ((TextView) findViewById(R.id.find_detail)).setText(finalJsonObject.getString("detial"));
        } else {
            findViewById(R.id.find_none).setVisibility(View.VISIBLE);
            findViewById(R.id.find_info).setVisibility(View.GONE);
        }

        new HGSelectViewAnimation(findViewById(R.id.face_image_find_info_more), findViewById(R.id.face_image_find_info_layout), findViewById(R.id.face_image_find_info_more));//下拉工具 基本信息

    }


    public void back(View view) {
        this.finish();
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
