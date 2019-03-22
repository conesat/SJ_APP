package com.hg.ui.builder;

import android.graphics.Bitmap;
import android.view.View;

import com.hg.ui.config.LayoutBounds;

public class HGBottomTabView {
    private View contentView;
    private Bitmap normalIcon;
    private Bitmap selectIcon;
    private String text;
    private LayoutBounds layoutBounds;

    /**
     *
     * @param contentView 主要内容页面布局
     * @param selectIcon 正常情况的按钮图片
     * @param normalIcon 选中的按钮图片
     * @param text 按钮文字
     */
    public HGBottomTabView(View contentView, Bitmap normalIcon,Bitmap selectIcon, String text, LayoutBounds layoutBounds) {
        this.contentView = contentView;
        this.normalIcon = normalIcon;
        this.selectIcon = selectIcon;
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

    public void setContentView(View contentView) {
        this.contentView = contentView;
    }


    public void setText(String text) {
        this.text = text;
    }

    public View getContentView() {
        return contentView;
    }

    public void setNormalIcon(Bitmap normalIcon) {
        this.normalIcon = normalIcon;
    }

    public void setSelectIcon(Bitmap selectIcon) {
        this.selectIcon = selectIcon;
    }

    public Bitmap getNormalIcon() {
        return normalIcon;
    }

    public Bitmap getSelectIcon() {
        return selectIcon;
    }

    public String getText() {
        return text;
    }
}
