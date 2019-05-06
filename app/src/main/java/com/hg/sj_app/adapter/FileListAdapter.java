package com.hg.sj_app.adapter;

import android.app.Activity;
import android.app.AlertDialog;
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

public class FileListAdapter extends BaseAdapter {
    private Context context;
    private List<FileData> files;
    private LayoutInflater layoutInflater;
    private EncryptionFile encryptionFileOpen;
    private EncryptionFile encryptionFileRecover;
    private PopupWindow popupWindow;
    private HGLoadDialog hgLoadDialog;
    private int POS = 0;
    private HttpHelper httpHelper;
    private Handler handler;

    public FileListAdapter(Context context, List<FileData> files,Handler handler) {
        this.context = context;
        this.files = files;
        this.encryptionFileOpen = new EncryptionFile(context);
        this.encryptionFileRecover = new EncryptionFile(context);
        this.handler=handler;
        httpHelper = new HttpHelper(context, "");
        encryptionFileOpen.setProcessListner(new EncryptionFile.ProcessListner() {
            @Override
            public void finish(String path, boolean done) {
                File file = new File(path);
                if (file.exists()) {

                    String type = FileTypeUtil.getMIMEType(file.getPath());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri uri = FileProvider.getUriForFile(context,
                                BuildConfig.APPLICATION_ID + ".fileprovider",
                                file);
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        // 设置文件类型为Word
                        intent.setDataAndType(uri, type);
                    } else {
                        Uri uri = Uri.fromFile(file);
                        intent.setDataAndType(uri, type);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    context.startActivity(intent);
                }else if (path.equals("无权开启")) {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "抱歉您无权查看该文件", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        encryptionFileRecover.setProcessListner(new EncryptionFile.ProcessListner() {
            @Override
            public void finish(String path, boolean done) {
                hgLoadDialog.dismiss();
                if (!new File(path).exists()) {
                    if (path.equals("无权开启")) {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "无访问权限", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "写出失败，请检查容量是否充足！", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } else {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "已还原", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
        layoutInflater = LayoutInflater.from(context);
        View inflate = layoutInflater.inflate(R.layout.pop_file_opreation, null, false);
        inflate.findViewById(R.id.pop_recover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                hgLoadDialog = new HGLoadDialog.Builder(context).setHint("正在写出文件...").setOutside(false).create();
                hgLoadDialog.show();
                encryptionFileRecover.decryptFile(files.get(POS).getPath(), Environment.getExternalStorageDirectory().getPath() + File.separator + "守机还原文件");

            }
        });
        inflate.findViewById(R.id.pop_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(context, AddFileActivity.class);
                intent.putExtra("uri", files.get(POS).getPath());
                intent.putExtra("type", files.get(POS).getType());
                intent.putExtra("opr", "edit");
                context.startActivity(intent);
            }
        });

        inflate.findViewById(R.id.pop_cloud).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                hgLoadDialog = new HGLoadDialog.Builder(context).setHint("文件上传中...").setOutside(false).create();
                hgLoadDialog.show();
                File file=new File(files.get(POS).getPath());
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", file.getName(),
                                RequestBody.create(MediaType.parse("multipart/form-data"), file))
                        .addFormDataPart("userId", StaticValues.USER_ID)
                        .addFormDataPart("fileType", files.get(POS).getType())
                        .addFormDataPart("fileName", files.get(POS).getName())
                        .build();
                httpHelper.post(StaticValues.HOST_URL + "/userFile/upload", requestBody, new Callback() {
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
                                if (jsonObject!=null&&jsonObject.getString("data")!=null) {
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
        inflate.findViewById(R.id.pop_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("确认");
                builder.setMessage("删除后无法恢复！是否删除？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new File(files.get(POS).getPath()).delete();
                        files.remove(POS);
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("否", null);
                builder.show();
            }
        });
        popupWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(false);
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
            View view = layoutInflater.inflate(R.layout.file_list_item, null);
            holder.name = view.findViewById(R.id.file_list_name);
            holder.menu = view.findViewById(R.id.file_list_menu);
            holder.detail = view.findViewById(R.id.file_detail);
            holder.parent = view.findViewById(R.id.file_list_name_parent);
            convertView = view;
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.name.setText(files.get(position).getName().replace(".hgd", ""));
        holder.detail.setText(files.get(position).getSize() + "   " + files.get(position).getDate());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (files.get(position).getType()) {
                    case "图片":
                        Intent intent = new Intent(context, ShowPictureActivity.class);
                        intent.putExtra("path", files.get(position).getPath());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        break;
                    default:
                        openFile(position);
                        break;
                }
            }
        });
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                POS = position;
                if (!popupWindow.isShowing()) {
                    popupWindow.showAsDropDown(v, -popupWindow.getWidth() - v.getWidth() * 2, 0);
                } else {
                    popupWindow.dismiss();
                }
            }
        });
        return convertView;
    }

    public void openFile(int pos) {
        new File(context.getExternalCacheDir() + File.separator + "files").delete();
        encryptionFileOpen.decryptFile(files.get(pos).getPath(), context.getExternalCacheDir() + File.separator + "files");
    }

    class Holder {
        public TextView name;
        public ImageView menu;
        public TextView detail;
        public LinearLayout parent;
    }


}
