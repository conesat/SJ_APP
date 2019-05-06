package com.hg.sj_app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
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
import com.androidev.download.DownloadManager;
import com.androidev.download.DownloadTask;
import com.hg.sj_app.R;
import com.hg.sj_app.activity.ShareEditActivity;
import com.hg.sj_app.activity.ShowPictureActivity;
import com.hg.sj_app.helper.HttpHelper;
import com.hg.sj_app.service.entity.UserFile;
import com.hg.sj_app.values.StaticValues;
import com.hg.ui.dialog.HGLoadDialog;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CloudFileListAdapter extends BaseAdapter {
    private Context context;
    private List<UserFile> files;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private HGLoadDialog hgLoadDialog;
    private int POS = 0;
    private HttpHelper httpHelper;
    private Handler handler;

    public CloudFileListAdapter(Context context, List<UserFile> files, Handler handler) {
        this.context = context;
        this.files = files;
        this.handler=handler;
        httpHelper = new HttpHelper(context, "");
        layoutInflater = LayoutInflater.from(context);
        View inflate = layoutInflater.inflate(R.layout.cloud_file_opreation, null, false);
        inflate.findViewById(R.id.cloud_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                DownloadManager controller = DownloadManager.getInstance();
                DownloadTask task = controller.newTask(new Date().getTime(), StaticValues.HOST_URL + "/userFile/getFile?path=" + files.get(POS).getFileUrl()
                        , files.get(POS).getFileName()).create();
                task.start();
                Toast.makeText(context, "已添加到下载列表", Toast.LENGTH_SHORT).show();
            }
        });
        inflate.findViewById(R.id.cloud_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                StaticValues.USER_FILE=files.get(POS);
                context.startActivity(new Intent(context, ShareEditActivity.class));
            }
        });

        inflate.findViewById(R.id.cloud_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("确认");
                builder.setMessage("删除后无法恢复！是否删除？");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hgLoadDialog = new HGLoadDialog.Builder(context).setHint("连接中...").setOutside(false).create();
                        hgLoadDialog.show();
                        httpHelper.get(StaticValues.HOST_URL + "/userFile/delete?userId=" + StaticValues.USER_ID+"&fileId="+files.get(POS).getFileId(), new Callback() {
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
                                            files.remove(POS);
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
        holder.name.setText(files.get(position).getFileName().replace(".hgd", ""));
        holder.detail.setText(toSize(files.get(position).getFileSize()) + "   " + files.get(position).getFileDate());

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



    class Holder {
        public TextView name;
        public ImageView menu;
        public TextView detail;
        public LinearLayout parent;
    }

    public String toSize(double size) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (size > 1024 * 1024 * 1024) {
            return df.format(size / (1024 * 1024 * 1024)) + "GB";
        } else if (size > 1024 * 1024) {
            return df.format(size / (1024 * 1024)) + "MB";
        } else if (size > 1024) {
            return df.format(size / (1024)) + "KB";
        } else {
            return size + "B";
        }
    }

}
