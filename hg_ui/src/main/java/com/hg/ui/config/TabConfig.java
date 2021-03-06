package com.hg.ui.config;

public class TabConfig {
    private int speed = 1500;
    private int style = -1;
    private boolean bottmShow = true;
    private boolean isNoScroll = false;
    private boolean onClickTransformer = true;//点击切换时开启效果

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

    public boolean isNoScroll() {
        return isNoScroll;
    }

    public void setNoScroll(boolean noScroll) {
        isNoScroll = noScroll;
    }

    public boolean isBottmShow() {
        return bottmShow;
    }

    public boolean isOnClickTransformer() {
        return onClickTransformer;
    }

    public void setOnClickTransformer(boolean onClickTransformer) {
        this.onClickTransformer = onClickTransformer;
    }

}
