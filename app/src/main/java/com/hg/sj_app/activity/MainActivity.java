package com.hg.sj_app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.hg.viewpagertab.ViewPagerTab;
import com.hg.viewpagertab.config.LayoutBounds;
import com.hg.viewpagertab.config.TabConfig;
import com.hg.viewpagertab.view.PageView;
import com.hg.sj_app.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPagerTab homeTab;
    private LinearLayout mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView(){
        mainView=findViewById(R.id.main_view);//在该视图下添加

        TabConfig tabConfig =new TabConfig();//配置信息
        tabConfig.setSpeed(500);//翻页时间 速度
        tabConfig.setStyle(4);//翻页风格  封装来源 https://github.com/AndroidMsky/ViewPagerAnimation
        tabConfig.setTabHeight(70); //底部按钮高度
        tabConfig.setTextSize(13); //底部文字大小

        List<PageView> list=new ArrayList<>();//视图列表
        //参数分别为 layout布局文件 不能为null，按钮未选中的图片 不能为null，按钮选中的图片 可为null，按钮文字 可为null ，按钮布局margin 可为null
        PageView news=new PageView(R.layout.tab_news_layout,R.drawable.news,R.drawable.news_1,"动态",new LayoutBounds(0,10));
        list.add(news);
        PageView home=new PageView(R.layout.tab_home_layout,R.drawable.home,R.drawable.home_1,null,new LayoutBounds(0,10));
        list.add(home);
        PageView my=new PageView(R.layout.tab_my_layout,R.drawable.my,R.drawable.my_1,"我的",new LayoutBounds(0,10));
        list.add(my);
        //创建即初始化
        homeTab=new ViewPagerTab(this,mainView,list, tabConfig);

    }
}
