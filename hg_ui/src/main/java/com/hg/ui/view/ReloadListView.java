package com.hg.ui.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hg.ui.R;
import com.hg.ui.builder.ThemeBuilder;

public class ReloadListView extends ListView implements AbsListView.OnScrollListener {

    private View bottomview; //尾文件
    private View headview; //头文件
    private int totaItemCounts;//用于表示是下拉还是上拉
    private int lassVisible; //上拉
    private int firstVisible; //下拉
    private LoadListener loadListener; //接口回调
    private int bottomHeight;//尾文件高度
    private int headHeight; //头文件高度
    private int Yload;//位置
    boolean isLoading = false;//加载状态
    boolean isRefresh = false;//是否刷新

    private ProgressBar topProgressBar;
    private TextView headerTitle;

    private Handler handler;

    public ReloadListView(Context context) {
        super(context);
        Init(context);
    }

    public ReloadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Init(context);
    }

    public ReloadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Init(context);
    }

    private void Init(Context context) {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                loadComplete();
            }
        };
        //拿到头布局文件xml
        headview = LinearLayout.inflate(context, R.layout.hg_reload_list_view_head, null);
        topProgressBar = headview.findViewById(R.id.hg_reload_list_view_header_headprogress);

        if (ThemeBuilder.THEME.getTopTabConfig().getTabBackgroudColor() != Color.WHITE) {
            ColorStateList colorStateList = ColorStateList.valueOf(ThemeBuilder.THEME.getTopTabConfig().getTabBackgroudColor());
            topProgressBar.setIndeterminateTintList(colorStateList);
            topProgressBar.setIndeterminateTintMode(PorterDuff.Mode.SRC_ATOP);
        }


        headerTitle = headview.findViewById(R.id.hg_reload_list_view_title);
        //拿到尾布局文件
        bottomview = LinearLayout.inflate(context, R.layout.hg_reload_list_view_bottom, null);
        //测量尾文件高度
        bottomview.measure(0, 0);
        //拿到高度
        bottomHeight = bottomview.getMeasuredHeight();
        //隐藏view
        bottomview.setPadding(0, -bottomHeight, 0, 0);
        headview.measure(0, 0);
        headHeight = headview.getMeasuredHeight();
        headview.setPadding(0, -headHeight, 0, 0);
        //添加listview底部
        this.addFooterView(bottomview);
        //添加到listview头部
        this.addHeaderView(headview);
        //设置拉动监听
        this.setOnScrollListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Yload = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isLoading) {
                    int moveY = (int) ev.getY();
                    int paddingY = moveY - Yload - headHeight;
                    if (paddingY < headHeight * 2 && paddingY > 0) {
                        headerTitle.setVisibility(VISIBLE);
                        headerTitle.setText("下拉刷新........");
                        topProgressBar.setVisibility(View.GONE);
                        isRefresh = false;
                    }
                    if (paddingY > headHeight * 2) {
                        headerTitle.setVisibility(VISIBLE);
                        headerTitle.setText("松开刷新........");
                        topProgressBar.setVisibility(View.GONE);
                        isRefresh = true;
                    }
                    headview.setPadding(0, paddingY, 0, 0);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) {
           // NewsItemView newsItemView = (NewsItemView) getChildAt(1);
           /* if (newsItemView.getJcVideoPlayer() != null) {
                newsItemView.getJcVideoPlayer().pasue();
            }*/
        }
        if (totaItemCounts <= lassVisible + 1 && scrollState == SCROLL_STATE_IDLE) {
            if (!isLoading) {
                load();
                bottomview.setPadding(0, 0, 0, 0);
                //加载数据
                if (loadListener != null)
                    loadListener.onLoad();
                isLoading = true;
            }
        }
        if (firstVisible <= 1 && scrollState == SCROLL_STATE_IDLE) {
            if (!isLoading) {
                if (isRefresh) {
                    load();
                    headview.setPadding(0, 0, 0, 0);
                    topProgressBar.setVisibility(VISIBLE);
                    headerTitle.setVisibility(GONE);
                    if (loadListener != null)
                        loadListener.pullLoad();
                    isLoading = true;
                    isRefresh = false;
                } else {
                    headview.setPadding(0, -headHeight, 0, 0);
                }
            }
        }

    }

    private void load() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendMessage(new Message());
            }
        }.start();
    }


    //接口回调
    public interface LoadListener {
        void onLoad();

        void pullLoad();
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        this.firstVisible = firstVisibleItem;
        this.lassVisible = firstVisibleItem + visibleItemCount;
        this.totaItemCounts = totalItemCount;
    }

    //加载完成
    public void loadComplete() {
        isLoading = false;
        bottomview.setPadding(0, -bottomHeight, 0, 0);
        headview.setPadding(0, -headHeight, 0, 0);
    }

    public void setLoadListener(LoadListener loadListener) {
        this.loadListener = loadListener;
    }
}
