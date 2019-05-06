package com.hg.sj_app.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hg.sj_app.R;
import com.hg.sj_app.helper.DataHelper;
import com.hg.sj_app.values.StaticValues;
import com.hg.sj_app.view.MyView;
import com.hg.sj_app.view.ViewHome;
import com.hg.ui.HgUiLoader;
import com.hg.ui.animator.HGRotateAnimation;
import com.hg.ui.builder.HGBottomTabView;
import com.hg.ui.builder.HGTopTabView;
import com.hg.ui.builder.ThemeBuilder;
import com.hg.ui.config.BottomTabConfig;
import com.hg.ui.config.LayoutBounds;
import com.hg.ui.config.TopTabConfig;
import com.hg.ui.entity.HGNewsItemEntity;
import com.hg.ui.layout.HGBottomTab;
import com.hg.ui.layout.HGNewsListView;
import com.hg.ui.layout.HGTopTab;
import com.hg.ui.listener.HGScreenListener;
import com.hg.ui.listener.OnNewsItemClickListener;
import com.hg.ui.listener.OnSwapeListener;
import com.hg.ui.view.NewsItemView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private HGBottomTab hgBottomTab;
    private LinearLayout mainView;
    private HGTopTab hgTopTab;
    private HGNewsListView hgNewsListView;
    private HGNewsListView myFcousOn;
    private HGNewsListView hgNewsListViewHG;
    private HGNewsListView hgNewsListViewArround;

    private View newsView;
    private HGRotateAnimation viewHomeRotateAnimation;
    private HGRotateAnimation refreshRotateAnimation;

    private MyView myView;

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

        OnNewsItemClickListener onNewsItemClickListener = new OnNewsItemClickListener() {
            @Override
            public void onClick(NewsItemView newsItemView, int clickItem, HGNewsItemEntity hgNewsItemEntity) {
                StaticValues.hgNewsItemEntity=hgNewsItemEntity;
                if (clickItem==4||clickItem==8||clickItem==11){
                    startActivity(new Intent(MainActivity.this,NewsDetailActivity.class));
                }
            }
        };

        //第一个页面详细
        hgNewsListView = new HGNewsListView(this, onNewsItemClickListener);
        myFcousOn = new HGNewsListView(this, onNewsItemClickListener);
        hgNewsListViewHG=new HGNewsListView(this, onNewsItemClickListener);
        hgNewsListViewArround=new HGNewsListView(this, onNewsItemClickListener);

        List<HGTopTabView> hgTopTabViews = new ArrayList<>();//页面内容列表
        hgTopTabViews.add(new HGTopTabView("关注", myFcousOn));//添加一个预置动态布局
        hgTopTabViews.add(new HGTopTabView("推荐", hgNewsListView));
        hgTopTabViews.add(new HGTopTabView("官方", hgNewsListViewHG));
        hgTopTabViews.add(new HGTopTabView("周边", hgNewsListViewArround));


        TopTabConfig topTabConfig=new TopTabConfig();
        topTabConfig.setOnClickTransformer(false);
        topTabConfig.setNoScroll(true);
        hgTopTab = new HGTopTab(this, hgTopTabViews, topTabConfig, 1);
        newsView = LayoutInflater.from(this).inflate(R.layout.tab_news_layout, null);
        ((LinearLayout) newsView.findViewById(R.id.news)).addView(hgTopTab.getView());

        //第一个页面
        HGBottomTabView news = new HGBottomTabView(newsView,
                BitmapFactory.decodeResource(this.getResources(), R.drawable.news),
                BitmapFactory.decodeResource(this.getResources(), R.drawable.news_1),
                "动态", new LayoutBounds(0, 10));
        hgBottomTabViews.add(news);
        //第二个页面

        ViewHome viewHome=new ViewHome(this);
        viewHomeRotateAnimation = new HGRotateAnimation(viewHome.findViewById(R.id.home_loading_view), true, 4000, true);

        HGBottomTabView home = new HGBottomTabView(viewHome,
                BitmapFactory.decodeResource(this.getResources(), R.drawable.home),
                BitmapFactory.decodeResource(this.getResources(), R.drawable.home_1),
                null, new LayoutBounds(0, 10));
        hgBottomTabViews.add(home);
        //第三个页面
        myView = new MyView(this);
        HGBottomTabView my = new HGBottomTabView(myView,
                BitmapFactory.decodeResource(this.getResources(), R.drawable.my),
                BitmapFactory.decodeResource(this.getResources(), R.drawable.my_1),
                "我的", new LayoutBounds(0, 10));
        hgBottomTabViews.add(my);
        BottomTabConfig bottomTabConfig=new BottomTabConfig();
        bottomTabConfig.setOnClickTransformer(false);
        bottomTabConfig.setNoScroll(true);
        hgBottomTab = new HGBottomTab(this, hgBottomTabViews, bottomTabConfig,1);
        mainView.addView(hgBottomTab.getView());

        otherSetting();
    }


    public void otherSetting() {
        myFcousOn.setOnSwapeListener(new OnSwapeListener() {
            @Override
            public void onRefresh() {
                myFcousOn.refreshData(DataHelper.suiji(10));
            }

            @Override
            public void onLoadMore() {
                myFcousOn.loadData(DataHelper.suiji(10));
            }
        });

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
        hgNewsListViewHG.setOnSwapeListener(new OnSwapeListener() {
            @Override
            public void onRefresh() {
                hgNewsListViewHG.refreshData(DataHelper.suiji(10));
            }

            @Override
            public void onLoadMore() {
                hgNewsListViewHG.loadData(DataHelper.suiji(10));
            }
        });

        hgNewsListViewArround.setOnSwapeListener(new OnSwapeListener() {
            @Override
            public void onRefresh() {
                hgNewsListViewArround.refreshData(DataHelper.suiji(10));
            }

            @Override
            public void onLoadMore() {
                hgNewsListViewArround.loadData(DataHelper.suiji(10));
            }
        });

        refreshRotateAnimation = new HGRotateAnimation(newsView.findViewById(R.id.my_refresh), false, 1000, true);
        newsView.findViewById(R.id.my_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshRotateAnimation.start();
                hgNewsListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DataHelper.reload();
                        if (hgTopTab.getPosition()==0){
                            myFcousOn.refreshData(DataHelper.suiji(10));
                            myFcousOn.loadComplete();
                        }
                        if (hgTopTab.getPosition()==1){
                            hgNewsListView.refreshData(DataHelper.suiji(10));
                            hgNewsListView.loadComplete();
                        }
                        if (hgTopTab.getPosition()==2){
                            hgNewsListViewHG.refreshData(DataHelper.suiji(10));
                            hgNewsListViewHG.loadComplete();
                        }
                        if (hgTopTab.getPosition()==3){
                            hgNewsListViewArround.refreshData(DataHelper.suiji(10));
                            hgNewsListViewArround.loadComplete();
                        }
                        refreshRotateAnimation.stop();
                    }
                }, 2000);
            }
        });

        //监听搜索按钮点击
        hgTopTab.setSearchOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了搜索", Toast.LENGTH_LONG).show();
            }
        });

        (new HGScreenListener(this)).begin(new HGScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                viewHomeRotateAnimation.start();
            }

            @Override
            public void onScreenOff() {
            }

            @Override
            public void onUserPresent() {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        myView.resetName();
        viewHomeRotateAnimation.start();
    }
}
