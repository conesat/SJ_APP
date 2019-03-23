package com.hg.ui.builder;

import com.hg.ui.theme.BaseTheme;
import com.hg.ui.theme.BlueTheme;
import com.hg.ui.theme.NormalTheme;
import com.hg.ui.theme.RedTheme;

public enum  ThemeBuilder {

    NORMAL,BLUE,RED;

    public static BaseTheme THEME=new NormalTheme();

    /**
     * 构建全局主题 构建后不需要传递config 否则以config为准
     * @param themeBuilder
     */
    public static void builderTheme(ThemeBuilder themeBuilder){
        switch (themeBuilder){
            case NORMAL:
                THEME=new NormalTheme();
                break;
            case BLUE:
                THEME=new BlueTheme();
                break;
            case RED:
                THEME=new RedTheme();
                break;
        }
    }

    //有则加载上次主题没有则根据参数创建
    public static void loadTheme(ThemeBuilder themeBuilder){
        builderTheme(themeBuilder);
    }

}
