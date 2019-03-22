package com.hg.sj_app.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hg.sj_app.R;
import com.hg.ui.builder.HGBottomTabView;
import com.hg.ui.builder.HGTopTabView;
import com.hg.ui.builder.ThemeBuilder;
import com.hg.ui.config.LayoutBounds;
import com.hg.ui.view.HGBottomTab;
import com.hg.ui.view.HGTopTab;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HGBottomTab hgBottomTab;
    private LinearLayout mainView;
    private HGTopTab hgTopTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView() {
        mainView = findViewById(R.id.main_view);//在该视图下添加

        List<HGBottomTabView> hgBottomTabViews = new ArrayList<>();//视图列表

        ThemeBuilder.builderTheme(ThemeBuilder.BLUE);//创建全局主题

        List<HGTopTabView> hgTopTabViews = new ArrayList<>();
        hgTopTabViews.add(new HGTopTabView("关注", LayoutInflater.from(MainActivity.this).inflate(R.layout.hg_list_view_item, null)));
        hgTopTabViews.add(new HGTopTabView("推荐", LayoutInflater.from(MainActivity.this).inflate(R.layout.hg_list_view_item, null)));
        hgTopTabViews.add(new HGTopTabView("官方", LayoutInflater.from(MainActivity.this).inflate(R.layout.hg_list_view_item, null)));
        hgTopTabViews.add(new HGTopTabView("周边", LayoutInflater.from(MainActivity.this).inflate(R.layout.hg_list_view_item, null)));

        hgTopTab = new HGTopTab(this, hgTopTabViews, null);
        hgTopTab.setSearchOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"dd",Toast.LENGTH_LONG).show();
            }
        });

        View newsView=LayoutInflater.from(this).inflate(R.layout.tab_news_layout,null);
        ((LinearLayout)newsView.findViewById(R.id.news)).addView( hgTopTab.getView());


        HGBottomTabView news = new HGBottomTabView(newsView,
                BitmapFactory.decodeResource(this.getResources(),R.drawable.news),
                BitmapFactory.decodeResource(this.getResources(),R.drawable.news_1),
                "动态", new LayoutBounds(0, 10));

        hgBottomTabViews.add(news);
        HGBottomTabView home = new HGBottomTabView(LayoutInflater.from(this).inflate(R.layout.tab_home_layout,null),
                BitmapFactory.decodeResource(this.getResources(),R.drawable.home),
                BitmapFactory.decodeResource(this.getResources(),R.drawable.home_1),
                null, new LayoutBounds(0, 10));

        hgBottomTabViews.add(home);
        HGBottomTabView my = new HGBottomTabView(LayoutInflater.from(this).inflate(R.layout.tab_my_layout,null),
                BitmapFactory.decodeResource(this.getResources(),R.drawable.my),
                BitmapFactory.decodeResource(this.getResources(),R.drawable.my_1),
                "我的", new LayoutBounds(0, 10));
        hgBottomTabViews.add(my);

        hgBottomTab = new HGBottomTab(this, hgBottomTabViews, null);
        mainView.addView(hgBottomTab.getView());

    }

}
