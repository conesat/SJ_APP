package com.hg.sj_app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import com.hg.hometab.ViewPagerTab;
import com.hg.hometab.config.LayoutBounds;
import com.hg.hometab.config.TabConfig;
import com.hg.hometab.view.PageView;
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
        mainView=findViewById(R.id.main_view);

        TabConfig tabConfig =new TabConfig();
        tabConfig.setSpeed(1500);
        tabConfig.setStyle(0);
        tabConfig.setTabHeight(50);
        tabConfig.setTextSize(10);

        List<PageView> list=new ArrayList<>();
        PageView news=new PageView(R.layout.tab_news_layout,R.drawable.news,R.drawable.news_1,"动态",new LayoutBounds(0,10));
        list.add(news);
        PageView home=new PageView(R.layout.tab_home_layout,R.drawable.home,R.drawable.home_1,null,new LayoutBounds(0,10));
        list.add(home);
        PageView my=new PageView(R.layout.tab_my_layout,R.drawable.my,R.drawable.my_1,"我的",new LayoutBounds(0,10));
        list.add(my);

        homeTab=new ViewPagerTab(this,mainView,list, tabConfig);

/*        homeTab.initView(0, new InitView() {
            @Override
            public void init(PageViewProtect pageViewProtect) {
                TextView textView=pageViewProtect.getContentView().findViewById(R.id.test);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,"s",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });*/
      /*  homeTab.setOnHomeTabChangeListener(new OnHomeTabChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i, PageViewProtect pageView) {
                Toast.makeText(MainActivity.this,""+i,Toast.LENGTH_LONG).show();
                pageView.getContentView().setBackgroundColor(Color.RED);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });*/

    }
}
