package com.hg.ui.animator;

import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class HGSelectViewAnimation {
    private View controlleView;
    private View includeView;
    private ImageView moreIcon;
    private Drawable onOpendImg, onCloseImg;
    private int HEIGHT = 0;

    /**
     * 收起布局 用于下拉
     * @param controlleView 控制收起的视图
     * @param includeView 需要收起显示的视图
     * @param moreIcon 按钮图片
     * @param onOpendImg 开启时的按钮图片
     * @param onCloseImg 收起时的按钮图片
     */
    public HGSelectViewAnimation(View controlleView, View includeView, ImageView moreIcon, Drawable onOpendImg, Drawable onCloseImg) {
        this.controlleView = controlleView;
        this.includeView = includeView;
        this.moreIcon = moreIcon;
        this.onOpendImg = onOpendImg;
        this.onCloseImg = onCloseImg;
        initView();
    }


    /**
     * 收起布局 用于下拉
     * @param controlleView 控制收起的视图
     * @param includeView 需要收起显示的视图
     * @param moreIcon 按钮图片
     */
    public HGSelectViewAnimation(View controlleView, View includeView, ImageView moreIcon) {
        this(controlleView, includeView, moreIcon, null, null);
    }

    private void initView() {
        includeView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                HEIGHT = includeView.getHeight();
                includeView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        controlleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showView();
            }
        });
    }

    ValueAnimator showSV;


    public void showView() {
        if (includeView.getHeight()>=HEIGHT){
            showView(false);
        }else {
            showView(true);
        }
    }

    public void showView(boolean show) {
        if (showSV != null) {
            showSV.cancel();
        }
        if (show) {
            showSV = ValueAnimator.ofInt(0, HEIGHT);
            if (moreIcon != null) {
                if (onCloseImg == null) {
                    new HGRotateAnimation(moreIcon, true, 300, false, 180f, 0f);
                } else {
                    moreIcon.setImageDrawable(onCloseImg);
                }
            }
        } else {
            showSV = ValueAnimator.ofInt(HEIGHT, 0);
            if (moreIcon != null) {
                if (onOpendImg == null) {
                    new HGRotateAnimation(moreIcon, true, 300, false, 0f, 180f);
                } else {
                    moreIcon.setImageDrawable(onOpendImg);
                }
            }
        }
        showSV.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                includeView.getLayoutParams().height = value;
                includeView.requestLayout();
            }
        });
        showSV.setDuration(300);
        showSV.start();
    }
}
