package com.hg.ui.builder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HGBottomTabViewProtect {
    private View contentView;
    private LinearLayout tabLayout;
    private ImageView icon;
    private TextView title;

    public HGBottomTabViewProtect(View contentView, LinearLayout tabLayout, ImageView icon, TextView title) {
        this.contentView = contentView;
        this.tabLayout = tabLayout;
        this.icon = icon;
        this.title = title;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public void setTabLayout(LinearLayout tabLayout) {
        this.tabLayout = tabLayout;
    }

    public void setIcon(ImageView icon) {
        this.icon = icon;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public View getContentView() {
        return contentView;
    }

    public LinearLayout getTabLayout() {
        return tabLayout;
    }

    public ImageView getIcon() {
        return icon;
    }

    public TextView getTitle() {
        return title;
    }
}
