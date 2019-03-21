package com.hg.hometab;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hg.hometab.config.TabConfig;
import com.hg.hometab.config.LayoutBounds;
import com.hg.hometab.listener.OnHomeTabChangeListener;
import com.hg.hometab.transformer.*;
import com.hg.hometab.util.DensityUtil;
import com.hg.hometab.util.InitView;
import com.hg.hometab.view.PageView;
import com.hg.hometab.view.PageViewProtect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPagerTab {
    private ViewPager viewPager; //对应的viewPager;
    private LinearLayout mainView;
    private List<PageView> viewList;//view数组
    private List<PageViewProtect> viewListP = new ArrayList<>();//view数组

    private List<ImageView> icons = new ArrayList<>();

    private TabConfig tabConfig;
    private LinearLayout bottmLayout;//底部按钮布局
    private Context context;
    private OnHomeTabChangeListener onHomeTabChangeListener;

    private Map<Integer, Integer> tabMap = new HashMap<>();

    public ViewPagerTab(Context context, LinearLayout mainView, List<PageView> viewList, TabConfig tabConfig) {
        if (tabConfig == null) {
            tabConfig = new TabConfig();
        } else {
            this.tabConfig = tabConfig;
        }
        if (tabConfig.getLayoutBounds() == null) {
            tabConfig.setLayoutBounds(new LayoutBounds());
        }
        this.mainView = mainView;
        this.viewList = viewList;
        this.context = context;
        initView();
    }

    private void initView() {
        viewPager = new ViewPager(context);
        bottmLayout = new LinearLayout(context);
        bottmLayout.setOrientation(LinearLayout.HORIZONTAL);

        ViewGroup.LayoutParams param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                DensityUtil.dip2px(context, tabConfig.getTabHeight()));
        bottmLayout.setLayoutParams(param);
        bottmLayout.setGravity(Gravity.CENTER);
        bottmLayout.setPadding(tabConfig.getLayoutBounds().getLeft(), tabConfig.getLayoutBounds().getTop(), tabConfig.getLayoutBounds().getRight(), tabConfig.getLayoutBounds().getBottom());
        bottmLayout.setBackgroundColor(tabConfig.getTabBackgroudColor());

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        for (int i = 0; i < viewList.size(); i++) {
            View content = layoutInflater.inflate(viewList.get(i).getContentViewId(), null);

            if (tabConfig.isBottmShow()) {
                final LinearLayout tabLayou = new LinearLayout(context);
                final ImageView icon = new ImageView(context);

                final TextView textView = new TextView(context);
                icon.setImageResource(viewList.get(i).getNormalIconId());

                textView.setTextSize(DensityUtil.dip2sp(context, tabConfig.getTextSize()));
                textView.setText(viewList.get(i).getText());
                textView.setGravity(Gravity.CENTER);

                tabLayou.setOrientation(LinearLayout.VERTICAL);
                tabLayou.setGravity(Gravity.CENTER);

                param = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                if (viewList.get(i).getText() != null) {
                    ((LinearLayout.LayoutParams) param).setMargins(viewList.get(i).getLayoutBounds().getLeft(), viewList.get(i).getLayoutBounds().getTop(), viewList.get(i).getLayoutBounds().getRight(), viewList.get(i).getLayoutBounds().getBottom());
                }else {
                    ((LinearLayout.LayoutParams) param).setMargins(viewList.get(i).getLayoutBounds().getLeft(), 0, viewList.get(i).getLayoutBounds().getRight(), viewList.get(i).getLayoutBounds().getBottom());
                }
                tabLayou.setLayoutParams(param);

                param = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
                icon.setLayoutParams(param);

                tabLayou.addView(icon);
                if (viewList.get(i).getText() != null) {
                    tabLayou.addView(textView);
                }



                bottmLayout.addView(tabLayou);

                PageViewProtect viewProtect = new PageViewProtect(content, tabLayou, icon, textView);

                viewListP.add(viewProtect);

                icons.add(icon);

                final int finalI = i;
                tabLayou.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onHomeTabChangeListener != null) {
                            onHomeTabChangeListener.onTabClick(finalI, tabLayou, icon, textView);
                        } else {
                            resetIcon(finalI);
                            setPage(finalI);
                        }
                    }
                });
            } else {
                PageViewProtect viewProtect = new PageViewProtect(content, null, null, null);
                viewListP.add(viewProtect);
            }
            setPage(0);
        }

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return viewListP.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(viewListP.get(position).getContentView());
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewListP.get(position).getContentView());
                return viewListP.get(position).getContentView();
            }
        };
        switch (tabConfig.getStyle()) {
            case 0:
                viewPager.setPageTransformer(true,
                        new AccordionTransformer());
                break;
            case 1:
                viewPager.setPageTransformer(true,
                        new BackgroundToForegroundTransformer());

                break;
            case 2:
                viewPager.setPageTransformer(true,
                        new CubeInTransformer());
                break;
            case 3:
                viewPager.setPageTransformer(true,
                        new CubeOutTransformer());
                break;
            case 4:
                viewPager.setPageTransformer(true,
                        new DepthPageTransformer());
                break;
            case 5:
                viewPager.setPageTransformer(true,
                        new FlipHorizontalTransformer());
                break;
            case 6:
                viewPager.setPageTransformer(true,
                        new FlipVerticalTransformer());
                break;
            case 7:
                viewPager.setPageTransformer(true,
                        new ForegroundToBackgroundTransformer());
                break;
            case 8:
                viewPager.setPageTransformer(true,
                        new RotateDownTransformer());
                break;
            case 9:
                viewPager.setPageTransformer(true,
                        new ScaleInOutTransformer());
                break;
            case 10:
                viewPager.setPageTransformer(true,
                        new StackTransformer());
                break;
            case 11:
                viewPager.setPageTransformer(true,
                        new TabletTransformer());
                break;
            case 12:
                viewPager.setPageTransformer(true,
                        new ZoomInTransformer());
                break;
            case 13:
                viewPager.setPageTransformer(true,
                        new ZoomOutSlideTransformer());
                break;
            case 14:
                viewPager.setPageTransformer(true,
                        new ZoomOutTranformer());
                break;
            case 15:
                viewPager.setPageTransformer(true,
                        new RotateUpTransformer());
                break;
            default:
                break;
        }
        viewPager.setAdapter(pagerAdapter);

        ViewPagerScroller pagerScroller = new ViewPagerScroller(context);
        pagerScroller.setScrollDuration(tabConfig.getSpeed());//设置时间，时间越长，速度越慢
        pagerScroller.initViewPagerScroll(viewPager);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if (onHomeTabChangeListener != null) {
                    onHomeTabChangeListener.onPageScrolled(i, v, i1);
                }
            }

            @Override
            public void onPageSelected(int i) {
                if (onHomeTabChangeListener != null) {
                    onHomeTabChangeListener.onPageSelected(i, viewListP.get(i));
                } else {
                    resetIcon(i);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (onHomeTabChangeListener != null) {
                    onHomeTabChangeListener.onPageScrollStateChanged(i);
                }
            }
        });

        mainView.setOrientation(LinearLayout.VERTICAL);
        param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        viewPager.setLayoutParams(param);


        mainView.addView(viewPager);
        mainView.addView(bottmLayout);
    }


    public void setOnHomeTabChangeListener(OnHomeTabChangeListener onHomeTabChangeListener) {
        this.onHomeTabChangeListener = onHomeTabChangeListener;
    }

    public void setPage(int page) {
        resetIcon(page);
        viewPager.setCurrentItem(page);
    }

    private void resetIcon(int page) {
        for (int i = 0; i < icons.size(); i++) {
            icons.get(i).setImageResource(viewList.get(i).getNormalIconId());
        }
        if (viewList.get(page).getSelectIconId() != null) {
            viewListP.get(page).getIcon().setImageResource(viewList.get(page).getSelectIconId());
        }
    }

    /**
     * 初始化视图
     *
     * @param i        第几个视图
     * @param initView 视图初始化回调
     */
    public void initView(int i, InitView initView) {
        initView.init(viewListP.get(i));
    }

}
