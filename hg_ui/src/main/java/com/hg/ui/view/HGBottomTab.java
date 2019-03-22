package com.hg.ui.view;

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

import com.hg.ui.builder.HGBottomTabView;
import com.hg.ui.builder.HGBottomTabViewProtect;
import com.hg.ui.builder.ThemeBuilder;
import com.hg.ui.config.BottomTabConfig;
import com.hg.ui.listener.OnBottomTabChangeListener;
import com.hg.ui.transformer.AccordionTransformer;
import com.hg.ui.transformer.BackgroundToForegroundTransformer;
import com.hg.ui.transformer.CubeInTransformer;
import com.hg.ui.transformer.CubeOutTransformer;
import com.hg.ui.transformer.DepthPageTransformer;
import com.hg.ui.transformer.FlipHorizontalTransformer;
import com.hg.ui.transformer.FlipVerticalTransformer;
import com.hg.ui.transformer.ForegroundToBackgroundTransformer;
import com.hg.ui.transformer.RotateDownTransformer;
import com.hg.ui.transformer.RotateUpTransformer;
import com.hg.ui.transformer.ScaleInOutTransformer;
import com.hg.ui.transformer.StackTransformer;
import com.hg.ui.transformer.TabletTransformer;
import com.hg.ui.transformer.ZoomInTransformer;
import com.hg.ui.transformer.ZoomOutSlideTransformer;
import com.hg.ui.transformer.ZoomOutTranformer;
import com.hg.ui.util.DensityUtil;
import com.hg.ui.util.ViewPagerScroller;

import java.util.ArrayList;
import java.util.List;

public class HGBottomTab {
    private ViewPager viewPager; //对应的viewPager;
    private LinearLayout mainView;
    private List<HGBottomTabView> viewList;//view数组
    private List<HGBottomTabViewProtect> viewListP = new ArrayList<>();//view数组

    private List<ImageView> icons = new ArrayList<>();

    private BottomTabConfig tabConfig;
    private LinearLayout bottmLayout;//底部按钮布局
    private Context context;
    private OnBottomTabChangeListener onBottomTabChangeListener;

    public HGBottomTab(Context context, List<HGBottomTabView> viewList, BottomTabConfig tabConfig) {
        if (tabConfig == null) {
            this.tabConfig = ThemeBuilder.THEME.getBottomTabConfig();
        } else {
            this.tabConfig = tabConfig;
        }

        this.viewList = viewList;
        this.context = context;
        mainView = new LinearLayout(context);
        initView();

    }

    public View getView() {
        return mainView;
    }

    private void initView() {
        viewPager = new ViewPager(context);
        viewPager.setOffscreenPageLimit(3);
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
            final LinearLayout tabLayou = new LinearLayout(context);
            final ImageView icon = new ImageView(context);

            final TextView textView = new TextView(context);

            icon.setImageBitmap(viewList.get(i).getNormalIcon());

            textView.setTextSize(DensityUtil.dip2sp(context, tabConfig.getTextSize()));
            textView.setText(viewList.get(i).getText());
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(tabConfig.getTabTextColor());
            tabLayou.setOrientation(LinearLayout.VERTICAL);
            tabLayou.setGravity(Gravity.CENTER);

            param = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
            if (viewList.get(i).getText() != null) {
                ((LinearLayout.LayoutParams) param).setMargins(viewList.get(i).getLayoutBounds().getLeft(), viewList.get(i).getLayoutBounds().getTop(), viewList.get(i).getLayoutBounds().getRight(), viewList.get(i).getLayoutBounds().getBottom());
            } else {
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

            HGBottomTabViewProtect viewProtect = new HGBottomTabViewProtect(viewList.get(i).getContentView(), tabLayou, icon, textView);

            viewListP.add(viewProtect);

            icons.add(icon);

            final int finalI = i;
            tabLayou.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBottomTabChangeListener != null) {
                        onBottomTabChangeListener.onTabClick(finalI, tabLayou, icon, textView);
                    } else {
                        resetIcon(finalI);
                        setPage(finalI);
                    }
                }
            });

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
                if (onBottomTabChangeListener != null) {
                    onBottomTabChangeListener.onPageScrolled(i, v, i1);
                }
            }

            @Override
            public void onPageSelected(int i) {
                if (onBottomTabChangeListener != null) {
                    onBottomTabChangeListener.onPageSelected(i, viewListP.get(i));
                } else {
                    resetIcon(i);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (onBottomTabChangeListener != null) {
                    onBottomTabChangeListener.onPageScrollStateChanged(i);
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

        if (!tabConfig.isBottmShow())
            mainView.getChildAt(mainView.getChildCount() - 1).setVisibility(View.GONE);

    }


    public void setOnBottomTabChangeListener(OnBottomTabChangeListener onBottomTabChangeListener) {
        this.onBottomTabChangeListener = onBottomTabChangeListener;
    }

    public void setPage(int page) {
        resetIcon(page);
        viewPager.setCurrentItem(page);
    }

    private void resetIcon(int page) {
        for (int i = 0; i < icons.size(); i++) {
            icons.get(i).setImageBitmap(viewList.get(i).getNormalIcon());
        }
        if (viewList.get(page).getSelectIcon() != null) {
            viewListP.get(page).getIcon().setImageBitmap(viewList.get(page).getSelectIcon());
        }
    }

    public void setBottomShow(boolean b) {
        if (b) {
            mainView.getChildAt(mainView.getChildCount() - 1).setVisibility(View.VISIBLE);
        } else {
            mainView.getChildAt(mainView.getChildCount() - 1).setVisibility(View.GONE);
        }
    }

}
