package com.hg.hometab.config;

import android.graphics.Color;

public class TabConfig {

    private int speed = 2500;
    private int style = 1;
    private int tabHeight = 60;
    private int textSize = 12;
    private boolean bottmShow = true;
    private int tabBackgroudColor=Color.WHITE;
    private LayoutBounds layoutBounds;

    public void setTabBackgroudColor(int tabBackgroudColor) {
        this.tabBackgroudColor = tabBackgroudColor;
    }

    public int getTabBackgroudColor() {
        return tabBackgroudColor;
    }

    public void setBottmShow(boolean bottmShow) {
        this.bottmShow = bottmShow;
    }

    public boolean isBottmShow() {
        return bottmShow;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextSize() {
        return textSize;
    }


    public void setTabHeight(int tabHeight) {
        this.tabHeight = tabHeight;
    }

    public int getTabHeight() {
        return tabHeight;
    }

    public void setLayoutBounds(LayoutBounds layoutBounds) {
        this.layoutBounds = layoutBounds;
    }

    public LayoutBounds getLayoutBounds() {
        return layoutBounds;
    }

    public TabConfig setSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    public TabConfig setStyle(int style) {
        this.style = style;
        return this;
    }

    public int getSpeed() {
        return speed;
    }

    public int getStyle() {
        return style;
    }
}
