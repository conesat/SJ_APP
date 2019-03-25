package com.hg.sj_app.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hg.sj_app.R;
import com.hg.sj_app.tools.DataHelper;
import com.hg.ui.HgUiLoader;
import com.hg.ui.builder.HGBottomTabView;
import com.hg.ui.builder.HGTopTabView;
import com.hg.ui.builder.ThemeBuilder;
import com.hg.ui.config.LayoutBounds;
import com.hg.ui.layout.HGBottomTab;
import com.hg.ui.layout.HGNewsListView;
import com.hg.ui.layout.HGTopTab;
import com.hg.ui.listener.OnSwapeListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HGBottomTab hgBottomTab;
    private LinearLayout mainView;
    private HGTopTab hgTopTab;
    private HGNewsListView hgNewsListView;
    private View newsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HgUiLoader.loadUi(this);
        initView();
    }

    public void initView() {
        mainView = findViewById(R.id.main_view);//在该视图下添加

        final List<HGBottomTabView> hgBottomTabViews = new ArrayList<>();//主视图列表
        ThemeBuilder.builderTheme(ThemeBuilder.BLUE);//创建全局主题

        //第一个页面详细
        hgNewsListView = new HGNewsListView(this, DataHelper.suiji(10));

        List<HGTopTabView> hgTopTabViews = new ArrayList<>();//页面内容列表
        hgTopTabViews.add(new HGTopTabView("关注", LayoutInflater.from(MainActivity.this).inflate(R.layout.test, null)));//添加一个预置动态布局
        hgTopTabViews.add(new HGTopTabView("推荐", hgNewsListView));
        hgTopTabViews.add(new HGTopTabView("官方", LayoutInflater.from(MainActivity.this).inflate(R.layout.hg_news_item, null)));
        hgTopTabViews.add(new HGTopTabView("周边", LayoutInflater.from(MainActivity.this).inflate(R.layout.hg_news_item, null)));


        hgTopTab = new HGTopTab(this, hgTopTabViews, null, 1);
        newsView = LayoutInflater.from(this).inflate(R.layout.tab_news_layout, null);
        ((LinearLayout) newsView.findViewById(R.id.news)).addView(hgTopTab.getView());

        //第一个页面
        HGBottomTabView news = new HGBottomTabView(newsView,
                BitmapFactory.decodeResource(this.getResources(), R.drawable.news),
                BitmapFactory.decodeResource(this.getResources(), R.drawable.news_1),
                "动态", new LayoutBounds(0, 10));
        hgBottomTabViews.add(news);
        //第二个页面
        HGBottomTabView home = new HGBottomTabView(LayoutInflater.from(this).inflate(R.layout.tab_home_layout, null),
                BitmapFactory.decodeResource(this.getResources(), R.drawable.home),
                BitmapFactory.decodeResource(this.getResources(), R.drawable.home_1),
                null, new LayoutBounds(0, 10));
        hgBottomTabViews.add(home);
        //第三个页面
        HGBottomTabView my = new HGBottomTabView(LayoutInflater.from(this).inflate(R.layout.tab_my_layout, null),
                BitmapFactory.decodeResource(this.getResources(), R.drawable.my),
                BitmapFactory.decodeResource(this.getResources(), R.drawable.my_1),
                "我的", new LayoutBounds(0, 10));
        hgBottomTabViews.add(my);

        hgBottomTab = new HGBottomTab(this, hgBottomTabViews, null);
        mainView.addView(hgBottomTab.getView());

        otherSetting();
    }


    public void otherSetting() {
        hgNewsListView.setOnSwapeListener(new OnSwapeListener() {
            @Override
            public void onRefresh() {
                hgNewsListView.refreshData(DataHelper.suiji(10));
            }

            @Override
            public void onLoadMore() {
                hgNewsListView.loadData(DataHelper.suiji(10));
            }
        });

        newsView.findViewById(R.id.my_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataHelper.reload();
                hgNewsListView.refreshData(DataHelper.suiji(10));
                hgNewsListView.loadComplete();
            }
        });

        //监听搜索按钮点击
        hgTopTab.setSearchOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "dd", Toast.LENGTH_LONG).show();
            }
        });
    }

}
