package com.hg.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.hg.ui.R;

public class RoundLinearlayout extends LinearLayout {

    public RoundLinearlayout(Context context) {
        this(context, null);
    }

    public RoundLinearlayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundLinearlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs
                , R.styleable.RoundLinearlayout);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);//形状

        gradientDrawable.setCornerRadius(array.getDimension(R.styleable.RoundLinearlayout_roundRadius, 10));
        gradientDrawable.setColor(array.getColor(R.styleable.RoundLinearlayout_bgColor, Color.WHITE));
        gradientDrawable.setStroke((int) array.getDimension(R.styleable.RoundLinearlayout_strokeWidth, 2),array.getColor(R.styleable.RoundLinearlayout_strokeColor, Color.WHITE));
        //记得使用完销毁
        array.recycle();
        this.setBackground(gradientDrawable);
    }

    //重新设置背景
    public void resetLayout(int radius,int color,int strokeWidth,int strokeColor){
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);//形状
        gradientDrawable.setCornerRadius(radius);
        gradientDrawable.setColor(color);
        gradientDrawable.setStroke(strokeWidth,strokeColor);
        this.setBackground(gradientDrawable);
    }
}
