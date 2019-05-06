package com.hg.sj_app.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.hg.sj_app.BuildConfig;
import com.hg.sj_app.R;
import com.hg.sj_app.activity.AddFileActivity;
import com.hg.sj_app.activity.ShowPictureActivity;
import com.hg.sj_app.helper.HttpHelper;
import com.hg.sj_app.module.FileData;
import com.hg.sj_app.service.entity.UserFileShare;
import com.hg.sj_app.util.EncryptionFile;
import com.hg.sj_app.util.FileTypeUtil;
import com.hg.sj_app.values.StaticValues;
import com.hg.ui.dialog.HGLoadDialog;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShareFileListAdapter extends BaseAdapter {
    private Context context;
    private List<UserFileShare> files;
    private LayoutInflater layoutInflater;
    private HGLoadDialog hgLoadDialog;
    private int POS = 0;
    private HttpHelper httpHelper;
    private Handler handler;

    public ShareFileListAdapter(Context context, List<UserFileShare> files, Handler handler) {
        this.context = context;
        this.files = files;
        this.handler = handler;
        httpHelper = new HttpHelper(context, "");
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return this.files.size();
    }

    @Override
    public Object getItem(int position) {
        return this.files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null || convertView.getTag() == null) {
            holder = new Holder();
            View view = layoutInflater.inflate(R.layout.my_share_item, null);
            holder.name = view.findViewById(R.id.my_share_file_name);
            holder.copy = view.findViewById(R.id.my_share_file_copy_code);
            holder.delete = view.findViewById(R.id.my_share_file_delete);
            convertView = view;
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.name.setText(files.get(position).getFileName().replace(".hgd", ""));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("确认");
                builder.setMessage("删除后无法恢复！是否删除？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // new File(files.get(POS).getPath()).delete();
                        hgLoadDialog = new HGLoadDialog.Builder(context).setHint("连接中...").setOutside(false).create();
                        hgLoadDialog.show();
                        httpHelper.get(StaticValues.HOST_URL + "/userFile/deleteShare?code="+files.get(position).getShareCode(), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        hgLoadDialog.dismiss();
                                        Toast.makeText(context, "连接失败", Toast.LENGTH_SHORT).show();
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
                                        if (jsonObject!=null&&jsonObject.getString("code").equals("0")) {
                                            files.remove(position);
                                            notifyDataSetChanged();
                                            Toast.makeText(context, jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });

                    }
                });
                builder.setNegativeButton("否", null);
                builder.show();
            }
        });
        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", files.get(position).getShareCode()+"");
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                Toast.makeText(context,"提取码已复制",Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }


    class Holder {
        public TextView name;
        public ImageView copy;
        public ImageView delete;
    }


}
