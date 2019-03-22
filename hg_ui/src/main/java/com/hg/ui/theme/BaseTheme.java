package com.hg.ui.theme;

import com.hg.ui.config.BottomTabConfig;
import com.hg.ui.config.TopTabConfig;

public class BaseTheme {
    private TopTabConfig topTabConfig;
    private BottomTabConfig bottomTabConfig;

    public BaseTheme() {
        this.topTabConfig = new TopTabConfig();
        this.bottomTabConfig = new BottomTabConfig();
    }

    public BaseTheme(TopTabConfig topTabConfig, BottomTabConfig bottomTabConfig) {
        this.topTabConfig = topTabConfig;
        this.bottomTabConfig = bottomTabConfig;
    }

    public void setTopTabConfig(TopTabConfig topTabConfig) {
        this.topTabConfig = topTabConfig;
    }

    public void setBottomTabConfig(BottomTabConfig bottomTabConfig) {
        this.bottomTabConfig = bottomTabConfig;
    }

    public TopTabConfig getTopTabConfig() {
        return topTabConfig;
    }

    public BottomTabConfig getBottomTabConfig() {
        return bottomTabConfig;
    }
}
