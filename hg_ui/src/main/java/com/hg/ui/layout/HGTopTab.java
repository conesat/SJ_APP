package com.hg.ui.layout;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hg.ui.R;
import com.hg.ui.builder.HGTopTabView;
import com.hg.ui.builder.ThemeBuilder;
import com.hg.ui.config.TopTabConfig;
import com.hg.ui.listener.OnTopTabChangeListener;
import com.hg.ui.tools.ViewPagerScroller;
import com.hg.ui.transformer.builder.TransformerBuilder;

import java.util.List;

public class HGTopTab {
    private Context context;
    private LinearLayout rootLayout;
    private List<HGTopTabView> hgTopTabViews;
    private ViewPager viewPager; //对应的viewPager;

    private TopTabConfig tabConfig;

    private LinearLayout tabs;

    private LinearLayout tabBottom;//底部滑动条

    private OnTopTabChangeListener onTopTabChangeListener;

    private ImageView search;

    private HorizontalScrollView horizontalScrollView;

    private Integer initTab = 0;

    public HGTopTab(Context context, List<HGTopTabView> hgTopTabViews, TopTabConfig tabConfig, Integer initTab) {
        this.context = context;
        this.rootLayout = new LinearLayout(context);
        this.hgTopTabViews = hgTopTabViews;
        if (tabConfig == null) {
            this.tabConfig = ThemeBuilder.THEME.getTopTabConfig();
        } else {
            this.tabConfig = tabConfig;
        }
        if (initTab < hgTopTabViews.size() && initTab >= 0) {
            this.initTab = initTab;
        }
        initView();
    }

    public View getView() {
        return rootLayout;
    }

    private void initView() {
        viewPager = new ViewPager(context);
        ViewGroup.LayoutParams param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        viewPager.setLayoutParams(param);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        LinearLayout mainView = layoutInflater.inflate(R.layout.hg_top_tab, null).findViewById(R.id.hg_top_tab_root);
        final View tabsView = layoutInflater.inflate(R.layout.hg_top_tab_tabs, null);
        horizontalScrollView = tabsView.findViewById(R.id.hg_top_tab_tabs_horizontal_scroll);
        tabsView.setPadding(tabConfig.getLayoutBounds().getLeft(), tabConfig.getLayoutBounds().getTop(), tabConfig.getLayoutBounds().getRight(), tabConfig.getLayoutBounds().getBottom());
        search = tabsView.findViewById(R.id.hg_top_tab_search);
        search.setColorFilter(tabConfig.getTabTextColor());
        if (!tabConfig.isShowSearch()) {
            search.setVisibility(View.GONE);
        }
        if (!tabConfig.isBottmShow()) {
            tabsView.setVisibility(View.GONE);
        }
        tabBottom = tabsView.findViewById(R.id.tab_bottm);
        tabBottom.setBackgroundColor(tabConfig.getTabTextColor());

        param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        tabsView.setLayoutParams(param);

        tabsView.setBackgroundColor(tabConfig.getTabBackgroudColor());

        tabs = tabsView.findViewById(R.id.hg_top_tab_tabs_texts);

        for (int i = 0; i < hgTopTabViews.size(); i++) {
            final View tabView = layoutInflater.inflate(R.layout.hg_top_tab_tabs_text, null);
            ((TextView) tabView.findViewById(R.id.hg_top_tab_tabs_text)).setText(hgTopTabViews.get(i).getTabText());
            ((TextView) tabView.findViewById(R.id.hg_top_tab_tabs_text)).setTextColor(tabConfig.getTabTextColor());
            tabs.addView(tabView);
           final int finalI = i;
            tabView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTabItem(finalI);
                }
            });

            if (i == initTab) {
                tabView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        setTabItem(initTab);
                        tabView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        }


        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return hgTopTabViews.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(hgTopTabViews.get(position).getContentView());
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(hgTopTabViews.get(position).getContentView());
                return hgTopTabViews.get(position).getContentView();
            }
        };

        TransformerBuilder.setViewPagerTransformer(viewPager, tabConfig.getStyle());

        viewPager.setAdapter(pagerAdapter);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (onTopTabChangeListener != null) {
                    onTopTabChangeListener.onPageScrolled(i, v, i1);
                }
            }

            @Override
            public void onPageSelected(int i) {
                if (onTopTabChangeListener != null) {
                    onTopTabChangeListener.onPageSelected(i, hgTopTabViews.get(i));
                } else {
                    setTabItem(i);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (onTopTabChangeListener != null) {
                    onTopTabChangeListener.onPageScrollStateChanged(i);
                }
            }
        });

        ViewPagerScroller pagerScroller = new ViewPagerScroller(context);
        pagerScroller.setScrollDuration(tabConfig.getSpeed());//设置时间，时间越长，速度越慢
        pagerScroller.initViewPagerScroll(viewPager);

        mainView.addView(tabsView);
        mainView.addView(viewPager);

        rootLayout.addView(mainView);
    }

    ValueAnimator locationVA;//位置动画
    ValueAnimator sizeVA;//大小动画


    public void setTabItem(int i) {
        if (locationVA != null) {
            locationVA.cancel();
        }
        if (sizeVA != null) {
            sizeVA.cancel();
        }
        sizeVA = ValueAnimator.ofInt(tabBottom.getWidth(), tabs.getChildAt(i).getWidth());
        locationVA = ValueAnimator.ofInt((int) tabBottom.getX(), (int) tabs.getChildAt(i).getX());

        locationVA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                tabBottom.setX(value);
                horizontalScrollView.smoothScrollTo((int) tabBottom.getX() - tabBottom.getWidth(), 0);
            }
        });
        locationVA.setDuration(700);
        locationVA.start();


        sizeVA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();
                tabBottom.getLayoutParams().width = value;
                tabBottom.requestLayout();
            }
        });
        sizeVA.setDuration(700);
        sizeVA.start();

        viewPager.setCurrentItem(i);
    }

    public void setOnTopTabChangeListener(OnTopTabChangeListener onTopTabChangeListener) {
        this.onTopTabChangeListener = onTopTabChangeListener;
    }

    public void setSearchOnclickListener(View.OnClickListener onclickListener) {
        search.setOnClickListener(onclickListener);
    }
}
