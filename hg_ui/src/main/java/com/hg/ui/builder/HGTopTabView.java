package com.hg.ui.builder;

import android.support.annotation.NonNull;
import android.view.View;

public class HGTopTabView {
    private String tabText;
    private View contentView;

    public void setTabText(String tabText) {
        this.tabText = tabText;
    }

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

    public String getTabText() {
        return tabText;
    }

    public View getContentView() {
        return contentView;
    }

    public HGTopTabView(String tabText,@NonNull View contentView) {
        this.tabText = tabText;
        this.contentView = contentView;
    }
}
