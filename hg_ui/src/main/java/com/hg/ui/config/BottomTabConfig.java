package com.hg.ui.config;

import android.graphics.Color;

public class BottomTabConfig extends TabConfig {
    private int tabHeight = 50;
    private int textSize = 11;
    private boolean onClickTransformer=true;//点击切换时开启效果
    private int tabBackgroudColor = Color.WHITE;
    private LayoutBounds layoutBounds = new LayoutBounds(10);

    public boolean isOnClickTransformer() {
        return onClickTransformer;
    }

    public void setOnClickTransformer(boolean onClickTransformer) {
        this.onClickTransformer = onClickTransformer;
    }

    public void setTabHeight(int tabHeight) {
        this.tabHeight = tabHeight;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setTabBackgroudColor(int tabBackgroudColor) {
        this.tabBackgroudColor = tabBackgroudColor;
    }

    public void setLayoutBounds(LayoutBounds layoutBounds) {
        this.layoutBounds = layoutBounds;
    }

    public void setTabTextColor(int tabTextColor) {
        this.tabTextColor = tabTextColor;
    }

    public int getTabHeight() {
        return tabHeight;
    }

    public int getTextSize() {
        return textSize;
    }

    public int getTabBackgroudColor() {
        return tabBackgroudColor;
    }

    public LayoutBounds getLayoutBounds() {
        return layoutBounds;
    }

    public int getTabTextColor() {
        return tabTextColor;
    }

    private int tabTextColor = Color.GRAY;
}
