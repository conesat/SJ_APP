package com.hg.ui.layout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.LinearLayout;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.hg.ui.R;
import com.hg.ui.adapter.RecyclerNewsListAdapter;
import com.hg.ui.entity.HGNewsItemEntity;
import com.hg.ui.listener.OnSwapeListener;

import java.util.List;

public class HGNewsListView extends LinearLayout {

    private List<HGNewsItemEntity> newsEntity;

    private RecyclerNewsListAdapter recyclerNewsListAdapter;

    private SwipeToLoadLayout swipeToLoadLayout;

    private OnSwapeListener onSwapeListener;

    private RecyclerView recyclerView;

    public HGNewsListView(Context context, List<HGNewsItemEntity> newsEntity) {
        super(context);
        this.newsEntity = newsEntity;
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.swipe_load_layout, this);

        recyclerNewsListAdapter = new RecyclerNewsListAdapter(getContext(), newsEntity);

        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
         swipeToLoadLayout.setLoadMoreEnabled(false);
        //swipeToLoadLayout.setLoadMoreTriggerOffset(200);
       // swipeToLoadLayout.setLoadMoreFinalDragOffset(200);
        recyclerView = (RecyclerView) findViewById(R.id.swipe_target);

        recyclerView.setAdapter(recyclerNewsListAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        recyclerView.setFocusableInTouchMode(false);

        recyclerView.setFocusable(false);

        recyclerView.setHasFixedSize(true);

        swipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (onSwapeListener != null) {
                    onSwapeListener.onRefresh();
                }
            }
        });

        swipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (onSwapeListener != null) {
                    onSwapeListener.onLoadMore();
                }
            }
        });

       recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isSlideToBottom(recyclerView)){
                    recyclerView.stopScroll();//该方法的调用起了关键作用
                    onSwapeListener.onLoadMore();
                   // swipeToLoadLayout.setLoadingMore(true);
                }
            }
        });

        autoRefresh();
    }

    public void postDelayed(Runnable runnable, int time) {
        swipeToLoadLayout.postDelayed(runnable, time);
        /*swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {

                mAdapter.add("REFRESH:\n" + new Date());
            }
        }, 2000);*/
    }

    public void setRefreshing(boolean refreshing) {
        swipeToLoadLayout.setRefreshing(refreshing);
    }

    public void refreshData(List<HGNewsItemEntity> newsEntity) {
        recyclerView.scrollToPosition(0);
    /*    LinearLayoutManager mLayoutManager =
                (LinearLayoutManager) recyclerView.getLayoutManager();
        mLayoutManager.scrollToPositionWithOffset(0, 0);*/
        this.newsEntity = newsEntity;
        loadComplete();
    }

    public void loadData(List<HGNewsItemEntity> newsEntity) {
        this.newsEntity.addAll(newsEntity);
        loadComplete();
    }

    public void loadComplete() {
        recyclerNewsListAdapter.setListItems(this.newsEntity);
        recyclerNewsListAdapter.notifyDataSetChanged();
        swipeToLoadLayout.setRefreshing(false);
        swipeToLoadLayout.setLoadingMore(false);
    }

    public void setOnSwapeListener(OnSwapeListener onSwapeListener) {
        this.onSwapeListener = onSwapeListener;
    }

    private void autoRefresh() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
                swipeToLoadLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.scrollToPosition(0);
                        loadComplete();
                    }
                }, 2000);
            }
        });
    }

    //监听滑动到底部
    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }
}
