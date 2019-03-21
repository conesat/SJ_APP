package com.hg.hometab.config;

import com.hg.hometab.util.DensityUtil;

public class LayoutBounds {
    private int left,  top,  right,  bottom;

    public LayoutBounds() {
        this.left = 10;
        this.top = 10;
        this.right = 10;
        this.bottom = 10;
    }

    /**
     * tab按钮内边距
     * @param all 全部
     */
    public LayoutBounds(int all) {
        this.left = all;
        this.top = all;
        this.right = all;
        this.bottom = all;
    }

    /**
     * tab按钮内边距
     * @param lr 左右
     * @param tb 上下
     */
    public LayoutBounds(int lr, int tb) {
        this.left = lr;
        this.top = tb;
        this.right = lr;
        this.bottom = tb;
    }

    /**
     * tab按钮内边距
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public LayoutBounds(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
    }

    public int getRight() {
        return right;
    }

    public int getBottom() {
        return bottom;
    }
}
