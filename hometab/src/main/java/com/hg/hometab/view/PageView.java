package com.hg.hometab.view;

import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.hg.hometab.config.LayoutBounds;

public class PageView {
    private int contentViewId;
    private Integer normalIconId;
    private Integer selectIconId;
    private String text;
    private LayoutBounds layoutBounds;

    /**
     *
     * @param contentViewId 主要内容页面布局
     * @param normalIconId 正常情况的按钮图片
     * @param selectIconId 选中的按钮图片
     * @param text 按钮文字
     */
    public PageView(@LayoutRes int contentViewId, @DrawableRes Integer normalIconId,@DrawableRes Integer selectIconId, String text,LayoutBounds layoutBounds) {
        this.contentViewId = contentViewId;
        this.normalIconId = normalIconId;
        this.selectIconId = selectIconId;
        this.text = text;
        if (layoutBounds==null){
            this.layoutBounds=new LayoutBounds(10);
        }else {
            this.layoutBounds=layoutBounds;
        }
    }

    public void setLayoutBounds(LayoutBounds layoutBounds) {
        this.layoutBounds = layoutBounds;
    }

    public LayoutBounds getLayoutBounds() {
        return layoutBounds;
    }

    public void setContentViewId(int contentViewId) {
        this.contentViewId = contentViewId;
    }

    public void setNormalIconId(Integer normalIconId) {
        this.normalIconId = normalIconId;
    }

    public void setSelectIconId(Integer selectIconId) {
        this.selectIconId = selectIconId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getContentViewId() {
        return contentViewId;
    }

    public Integer getNormalIconId() {
        return normalIconId;
    }

    public Integer getSelectIconId() {
        return selectIconId;
    }

    public String getText() {
        return text;
    }
}
