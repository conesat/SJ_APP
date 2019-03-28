package com.hg.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.hg.ui.R;

import java.util.List;

/**
 * Dev: xiongwenwei@aliyun.com
 * Time: 2017/9/21
 * Note: 加载中Dialog
 */

public class HGLoadDialog extends Dialog {

    private Context context;
    private final static int DURATION = 300;//帧动画时间间隔

    public HGLoadDialog(Context context) {
        super(context);
        this.context = context;
    }

    public static class Builder {

        private Context context;
        private String hint;
        private boolean isOutside;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 设置提示文字
         */
        public Builder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        /**
         * 点击四周是否消失
         */
        public Builder setOutside(boolean isOutside) {
            this.isOutside = isOutside;
            return this;
        }

        /**
         * 创建Dialog
         */
        public HGLoadDialog create() {
            HGLoadDialog dialog = new HGLoadDialog(context);
            //背景透明
            Window window = dialog.getWindow();
            window.setBackgroundDrawable(null);
            //初始化控件
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);

            TextView tvHint = (TextView) view.findViewById(R.id.tvHint);
            //提示文字
            if (!TextUtils.isEmpty(hint)) tvHint.setText(hint);
            //点击四周是否消失
            if (!isOutside) dialog.setCanceledOnTouchOutside(false);

            //设置视图
            dialog.setContentView(view);
            return dialog;
        }

        /**
         * 获取AnimationDrawable
         */
        private AnimationDrawable getAnimationDrawable(List<Integer> resIdList) {
            AnimationDrawable animationDrawable = new AnimationDrawable();
            for (int resId : resIdList) {
                Drawable drawable = ContextCompat.getDrawable(context, resId);
                animationDrawable.addFrame(drawable, DURATION);
            }
            animationDrawable.setOneShot(false);
            return animationDrawable;
        }

    }

}