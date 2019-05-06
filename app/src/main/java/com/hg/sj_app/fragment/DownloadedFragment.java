package com.hg.sj_app.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidev.download.DownloadInfo;
import com.androidev.download.DownloadJobListener;
import com.androidev.download.DownloadManager;
import com.hg.sj_app.BuildConfig;
import com.hg.sj_app.LoginActivity;
import com.hg.sj_app.R;
import com.hg.sj_app.util.BackEventHandler;
import com.hg.sj_app.util.EncryptionFile;
import com.hg.sj_app.util.FileManager;
import com.hg.sj_app.util.FileTypeUtil;
import com.hg.sj_app.util.FileUtilcll;
import com.hg.ui.dialog.HGLoadDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by 4ndroidev on 16/10/17.
 */
public class DownloadedFragment extends Fragment implements View.OnClickListener, BackEventHandler {

    private boolean isEditMode;
    private View hint;
    private View header;
    private View footer;
    private View contentView;
    private TextView summary;
    private TextView select;
    private DownloadAdapter adapter;
    private RecyclerView recyclerView;
    private List<DownloadInfo> downloads;
    private DownloadManager manager;
    private HGLoadDialog hgLoadDialog;
    private EncryptionFile encryptionFileOpen;
   // private FileManager fileManager;

    private DownloadJobListener jobListener = new DownloadJobListener() {
        @Override
        public void onCreated(DownloadInfo info) {
            //nothing to do
        }

        @Override
        public void onStarted(DownloadInfo info) {
            //nothing to do
        }

        @Override
        public void onCompleted(boolean finished, DownloadInfo info) {
            if (finished) {
                downloads.add(0, info);
                if (isEditMode) adapter.checks.add(0, false);
                adapter.notifyItemInserted(0);
                updateUI();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        downloads = new ArrayList<>();
        Context context = getContext();
        manager = DownloadManager.getInstance();
        manager.addDownloadJobListener(jobListener);
        List<DownloadInfo> infos = manager.getAllInfo();
        for (DownloadInfo info : infos) {
            if (!info.isFinished()) continue;
            downloads.add(info);
        }
        encryptionFileOpen = new EncryptionFile(context);
        encryptionFileOpen.setProcessListner(new EncryptionFile.ProcessListner() {
            @Override
            public void finish(String path, boolean done) {
                hgLoadDialog.dismiss();
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
                    startActivity(intent);
                } else if (path.equals("无权开启")) {
                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "抱歉您无权查看该文件", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView != null) return contentView;
        Context context = getContext();
        contentView = inflater.inflate(R.layout.fragment_downloaded, container, false);
        hint = contentView.findViewById(R.id.download_hint);
        header = contentView.findViewById(R.id.download_header);
        footer = contentView.findViewById(R.id.download_footer);
        summary = (TextView) contentView.findViewById(R.id.download_summary);
        select = (TextView) contentView.findViewById(R.id.download_select);
        recyclerView = (RecyclerView) contentView.findViewById(R.id.download_recycler_view);
        select.setOnClickListener(this);
        contentView.findViewById(R.id.download_delete).setOnClickListener(this);
        contentView.findViewById(R.id.download_manager).setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DownloadAdapter();
        recyclerView.setAdapter(adapter);
        updateUI();
        return contentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        manager.removeDownloadJobListener(jobListener);
        downloads.clear();
    }

    @Override
    public boolean onBackPressed() {
        if (isEditMode) {
            exitEditMode();
            return true;
        }
        return false;
    }

    private void updateUI() {
        Resources resources = getResources();
        summary.setText(resources.getString(R.string.download_summary, downloads.size()));
        boolean hasDownloads = downloads.size() > 0;
        if (!hasDownloads) {
            if (header.getVisibility() == View.VISIBLE) header.setVisibility(View.GONE);
            if (recyclerView.getVisibility() == View.VISIBLE) recyclerView.setVisibility(View.GONE);
            if (hint.getVisibility() != View.VISIBLE) hint.setVisibility(View.VISIBLE);
        } else {
            if (header.getVisibility() != View.VISIBLE) header.setVisibility(View.VISIBLE);
            if (recyclerView.getVisibility() != View.VISIBLE)
                recyclerView.setVisibility(View.VISIBLE);
            if (hint.getVisibility() == View.VISIBLE) hint.setVisibility(View.GONE);
        }
    }

    private void enterEditMode() {
        if (isEditMode) return;
        isEditMode = true;
        header.setVisibility(View.GONE);
        footer.setVisibility(View.VISIBLE);
        adapter.enterEditMode();
    }

    private void exitEditMode() {
        if (!isEditMode) return;
        isEditMode = false;
        header.setVisibility(View.VISIBLE);
        footer.setVisibility(View.GONE);
        adapter.exitEditMode();
    }

    private void deleteSelections() {
        final List<DownloadInfo> selections = adapter.getSelections();
        if (selections.size() == 0) {
            Toast.makeText(getContext(), R.string.download_select_waring, Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.download_delete_waring)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (DownloadInfo info : selections) manager.delete(info);
                        downloads.removeAll(selections);
                        exitEditMode();
                        updateUI();
                        dialog.dismiss();
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.download_manager == id) {
            enterEditMode();
        } else if (R.id.download_delete == id) {
            deleteSelections();
        } else if (R.id.download_select == id) {
            adapter.select();
        }
    }

    private class DownloadAdapter extends RecyclerView.Adapter<DownloadViewHolder> {

        private LayoutInflater inflater = LayoutInflater.from(getContext());
        private SimpleDateFormat format = new SimpleDateFormat(getResources().getString(R.string.download_date_format), Locale.CHINA);
        private List<Boolean> checks;

        @Override
        public DownloadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = inflater.inflate(R.layout.layout_downloaded_list_item, parent, false);
            return new DownloadViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(DownloadViewHolder holder, int position) {
            if (isEditMode) {
                if (holder.check.getVisibility() != View.VISIBLE)
                    holder.check.setVisibility(View.VISIBLE);
                if (holder.close.getVisibility() == View.VISIBLE) {
                    holder.close.setVisibility(View.GONE);
                }
                holder.check.setChecked(checks.get(position));
            } else {
                if (holder.check.getVisibility() == View.VISIBLE)
                    holder.check.setVisibility(View.GONE);
                if (holder.close.getVisibility() != View.VISIBLE) {
                    holder.close.setVisibility(View.VISIBLE);
                }
            }
            DownloadInfo info = downloads.get(position);
            holder.name.setText(info.name);
            holder.timestamp.setText(format.format(new Date(info.createTime)));
            holder.size.setText(String.format(Locale.US, "%.1fMB", info.contentLength / 1048576.0f));
       //     String extension = fileManager.getExtension(info.name);
            /*if (fileManager.isApk(extension)) {
                holder.icon.setImageResource(R.drawable.format_apk);
            } else if (fileManager.isMusic(extension)) {
                holder.icon.setImageResource(R.drawable.format_music);
            } else if (fileManager.isVideo(extension)) {
                holder.icon.setImageResource(R.drawable.format_vedio);
            } else if (fileManager.isZip(extension) || fileManager.isRar(extension)) {
                holder.icon.setImageResource(R.drawable.format_zip);
            } else {
                holder.icon.setImageResource(R.drawable.format_unknown);
            }*/
            holder.icon.setImageResource(R.mipmap.logo);
        }

        private void updateMenu() {
            if (isAllSelected()) {
                select.setText(R.string.label_select_none);
            } else {
                select.setText(R.string.label_select_all);
            }
        }

        private boolean isAllSelected() {
            for (Boolean bool : checks) {
                if (!bool) return false;
            }
            return true;
        }

        private void select() {
            boolean next = !isAllSelected();
            Collections.fill(checks, next);
            if (next) {
                select.setText(R.string.label_select_none);
            } else {
                select.setText(R.string.label_select_all);
            }
            notifyDataSetChanged();
        }

        private void enterEditMode() {
            int count = getItemCount();
            checks = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                checks.add(false);
            }
            select.setText(R.string.label_select_all);
            notifyDataSetChanged();
        }

        private void exitEditMode() {
            checks.clear();
            checks = null;
            notifyDataSetChanged();
        }

        private void toggle(int position) {
            if (position < 0 || position >= getItemCount()) return;
            checks.set(position, !checks.get(position));
            notifyItemChanged(position);
            updateMenu();
        }

        private List<DownloadInfo> getSelections() {
            if (!isEditMode) return new ArrayList<>();
            List<DownloadInfo> result = new ArrayList<>();
            for (int i = 0, count = getItemCount(); i < count; i++) {
                if (checks.get(i)) result.add(downloads.get(i));
            }
            return result;
        }

        @Override
        public int getItemCount() {
            return downloads == null ? 0 : downloads.size();
        }
    }

    private class DownloadViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckBox check;
        ImageView icon;
        TextView name;
        TextView size;
        TextView timestamp;
        View close;

        DownloadViewHolder(View itemView) {
            super(itemView);
            check = (CheckBox) itemView.findViewById(R.id.download_checkbox);
            icon = (ImageView) itemView.findViewById(R.id.download_icon);
            name = (TextView) itemView.findViewById(R.id.download_name);
            timestamp = (TextView) itemView.findViewById(R.id.download_timestamp);
            size = (TextView) itemView.findViewById(R.id.download_size);
            close = itemView.findViewById(R.id.download_close);
            close.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            if (v == itemView) {
                if (isEditMode) {
                    adapter.toggle(position);
                } else {
                    DownloadInfo info = downloads.get(position);
                    hgLoadDialog = new HGLoadDialog.Builder(getContext()).setHint("正在解密文件...").setOutside(false).create();
                    hgLoadDialog.show();
                    new File(getContext().getExternalCacheDir() + File.separator + "files").delete();
                    encryptionFileOpen.decryptFile(info.path, getContext().getExternalCacheDir() + File.separator + "files");
                }
            } else if (R.id.download_close == v.getId()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.download_delete_waring)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DownloadInfo info = downloads.get(position);
                                manager.delete(info);
                                downloads.remove(info);
                                adapter.notifyItemRemoved(position);
                                updateUI();
                                dialog.dismiss();
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
        }


    }


}
