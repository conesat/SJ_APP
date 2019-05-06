package com.hg.ui.config;

import android.graphics.Color;

public class TopTabConfig extends TabConfig {
    private int tabHeight = 40;
    private int textSize = 11;
    private int tabBackgroudColor = Color.rgb(2, 138, 255);
    private LayoutBounds layoutBounds = new LayoutBounds(50,20,50,10);
    private int tabTextColor = Color.WHITE;
    private boolean showSearch=true;

    public boolean isShowSearch() {
        return showSearch;
    }

    public void setShowSearch(boolean showSearch) {
        this.showSearch = showSearch;
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
}
