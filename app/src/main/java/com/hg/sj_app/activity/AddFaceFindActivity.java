package com.hg.sj_app.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.hg.sj_app.R;
import com.hg.sj_app.helper.HttpHelper;
import com.hg.sj_app.util.FileUtilcll;
import com.hg.sj_app.util.ImageUtil;
import com.hg.sj_app.values.StaticValues;
import com.hg.ui.dialog.HGButtomDialog;
import com.hg.ui.dialog.HGLoadDialog;
import com.hg.ui.listener.HGBottomDialogOnClickListener;
import com.hg.ui.ssq.ShengShiQuDialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddFaceFindActivity extends AppCompatActivity {
    private String name;
    private String birthday;
    private String idcard;
    private String city;
    private String sex;
    private String detail;

    private HttpHelper httpHelper;
    private Handler handler;
    private HGLoadDialog hgLoadDialog;
    private Uri uri;
    /**
     * 请求选择本地图片文件的请求码
     */
    private static final int ACTION_CHOOSE_IMAGE = 0x201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_face_find);
        handler = new Handler();
        httpHelper = new HttpHelper(this, "");
        hgLoadDialog = new HGLoadDialog.Builder(this).setHint("请稍等...").setOutside(false).create();
    }

    public void back(View view) {
        this.finish();
    }

    DatePickerDialog datePickerDialog;

    public void chooseDate(View birthday) {
        if (datePickerDialog == null) {
            //获得当前日期
            final Calendar calendar = Calendar.getInstance(Locale.CHINA);
            final int year = calendar.get(Calendar.YEAR);
            final int month = calendar.get(Calendar.MONTH);
            final int day = calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(this, R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    ((TextView) birthday).setText(year + "-" + month + "-" + dayOfMonth);
                }
            }, year, month, day);//创建弹框
        }
        datePickerDialog.show();//显示弹框
    }

    ShengShiQuDialog shengShiQuDialog;

    public void chooseCity(View view) {
        if (shengShiQuDialog == null) {
            shengShiQuDialog = new ShengShiQuDialog();
            shengShiQuDialog.setDataResultListener(new ShengShiQuDialog.ShengShiQuDialogListener() {
                @Override
                public void onSSQDialogResult(String province, String city, String district) {
                    ((TextView) view).setText(province + "-" + city + "-" + district);
                }
            });
        }
        shengShiQuDialog.show(getSupportFragmentManager(), "shengshiquDialog");
    }

    public void chooseImg(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, ACTION_CHOOSE_IMAGE);
    }

    HGButtomDialog sexDialog;

    public void chooseSex(View view) {
        if (sexDialog == null) {
            List<String> menus = new ArrayList<>();
            menus.add("男");
            menus.add("女");
            sexDialog = new HGButtomDialog(this, menus);
            sexDialog.setHgBottomDialogOnClickListener(new HGBottomDialogOnClickListener() {
                @Override
                public void onClick(View v, int i) {
                    ((TextView) view).setText(menus.get(i));
                    sexDialog.dismiss();
                }
            });
        }
        sexDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTION_CHOOSE_IMAGE) {
            if (data != null && data.getData() != null) {
                uri = data.getData();
                ((ImageView) findViewById(R.id.add_face_find_img)).setImageBitmap(ImageUtil.getBitmapFromUri(uri, this));
                return;
            }
        }
    }

    public void submit(View view) {
        name = ((EditText) findViewById(R.id.add_face_find_name)).getText().toString();
        birthday = ((TextView) findViewById(R.id.add_face_find_birthday)).getText().toString();
        idcard = ((EditText) findViewById(R.id.add_face_find_idcrad)).getText().toString();
        city = ((TextView) findViewById(R.id.add_face_find_city)).getText().toString();
        sex = ((TextView) findViewById(R.id.add_face_find_sex)).getText().toString();
        detail = ((EditText) findViewById(R.id.add_face_find_detail)).getText().toString();
        if (name.equals("")) {
            showToast("请输入姓名");
        } else if (birthday.equals("")) {
            showToast("请选择生日");
        } else if (idcard.equals("")) {
            showToast("请输入身份证");
        } else if (city.equals("")) {
            showToast("请选择籍贯");
        } else if (sex.equals("")) {
            showToast("请选择性别");
        } else if (detail.equals("")) {
            showToast("请输入详细信息");
        } else if (uri == null) {
            showToast("请选择图片");
        } else {
            File img = FileUtilcll.uriToFile(uri, this);
            hgLoadDialog.show();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", img.getName(),
                            RequestBody.create(MediaType.parse("multipart/form-data"), img))
                    .addFormDataPart("groupId", "101")
                    .addFormDataPart("name", name)
                    .addFormDataPart("sex", sex)
                    .addFormDataPart("birthday", birthday)
                    .addFormDataPart("idCard", idcard)
                    .addFormDataPart("city", city)
                    .addFormDataPart("type", "失联")
                    .addFormDataPart("sendCity", "")
                    .addFormDataPart("detail", detail)
                    .addFormDataPart("state", "-1")
                    .build();
            httpHelper.post(StaticValues.FACE_SERVICE_URL + "/faceAdd", requestBody, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            hgLoadDialog.dismiss();
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
                            hgLoadDialog.dismiss();
                            if (jsonObject.getString("code").equals("0")) {
                                showToast("已发送申请");
                            }
                        }
                    });
                }
            });
        }
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
