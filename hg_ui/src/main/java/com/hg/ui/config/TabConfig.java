package com.hg.ui.config;

public class TabConfig {
    private int speed = 1500;
    private int style = -1;
    private boolean bottmShow = true;

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public void setBottmShow(boolean bottmShow) {
        this.bottmShow = bottmShow;
    }

    public int getSpeed() {
        return speed;
    }

    public int getStyle() {
        return style;
    }

    public boolean isBottmShow() {
        return bottmShow;
    }
}
