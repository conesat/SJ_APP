package com.hg.ui.listener;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hg.ui.builder.HGBottomTabViewProtect;

public interface OnBottomTabChangeListener {

    void onPageScrolled(int i, float v, int i1);

    void onPageSelected(int i, HGBottomTabViewProtect pageViewProtect);

    void onPageScrollStateChanged(int i);

    void onTabClick(int i, LinearLayout layout,ImageView icon,TextView txt);
}
