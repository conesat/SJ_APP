package com.hg.hometab.listener;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hg.hometab.view.PageView;
import com.hg.hometab.view.PageViewProtect;

public interface OnHomeTabChangeListener {

    void onPageScrolled(int i, float v, int i1);

    void onPageSelected(int i, PageViewProtect pageViewProtect);

    void onPageScrollStateChanged(int i);

    void onTabClick(int i, LinearLayout layout,ImageView icon,TextView txt);
}
