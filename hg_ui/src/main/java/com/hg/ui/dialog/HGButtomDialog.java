package com.hg.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hg.ui.R;
import com.hg.ui.listener.HGBottomDialogOnClickListener;
import com.hg.ui.tools.DensityUtil;

import java.util.List;

public class HGButtomDialog extends Dialog {
    private boolean iscancelable;//控制点击dialog外部是否dismiss
    private boolean isBackCancelable;//控制返回键是否dismiss
    private boolean showClose = true;
    private List<String> menus;
    private HGBottomDialogOnClickListener hgBottomDialogOnClickListener;
    //  private View view;


    public HGButtomDialog(Context context, List<String> menus) {
        this(context, menus, true, true, true);
    }

    public HGButtomDialog(Context context, List<String> menus, boolean isCancelable, boolean isBackCancelable, boolean showClose) {
        super(context, R.style.MyDialog);
        this.menus = menus;
        this.showClose = showClose;
        this.iscancelable = isCancelable;
        this.isBackCancelable = isBackCancelable;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_dialog, null);
        LinearLayout menuLayout = view.findViewById(R.id.menus_layout);

        for (int i = 0; i < menus.size(); i++) {
            TextView menu = new TextView(getContext());
            int p = DensityUtil.dip2px(getContext(), 10);
            menu.setPadding(p, p, p, p);
            menu.setText(menus.get(i));
            menu.setTextColor(Color.rgb(116,116,116));
            menu.setTextSize(16);
            menu.setGravity(Gravity.CENTER);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            menu.setLayoutParams(lp);
            menuLayout.addView(menu);
            final int finalI = i;
            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hgBottomDialogOnClickListener!=null){
                        hgBottomDialogOnClickListener.onClick(v, finalI);
                    }
                }
            });
        }

        if (showClose) {
            view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }else {
            view.findViewById(R.id.close_layout).setVisibility(View.GONE);
        }

        setContentView(view);//这行一定要写在前面
        setCancelable(iscancelable);//点击外部不可dismiss
        setCanceledOnTouchOutside(isBackCancelable);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.windowAnimations = R.style.BottomDialogAnimation;
        window.setAttributes(params);
    }

    @Override
    public void show() {
        super.show();
    }

    public void setHgBottomDialogOnClickListener(HGBottomDialogOnClickListener hgBottomDialogOnClickListener) {
        this.hgBottomDialogOnClickListener = hgBottomDialogOnClickListener;
    }
}