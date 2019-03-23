package com.hg.ui.layout;

import android.content.Context;
import android.widget.ListView;

import com.hg.ui.adapter.ListViewAdapter;
import com.hg.ui.entity.HGNewsItemEntity;
import com.hg.ui.view.ReloadListView;

import java.util.List;

public class HGNewsListView {
    private Context context;
    private List<HGNewsItemEntity> newsEntity;
    private ReloadListView listView;
    private ListViewAdapter listViewAdapter;

    public HGNewsListView(Context context, List<HGNewsItemEntity> newsEntity) {
        this.context = context;
        this.newsEntity = newsEntity;
        initView();
    }

    private void initView() {
        listView = new ReloadListView(context);
        listViewAdapter = new ListViewAdapter(context, newsEntity);
        listView.setAdapter(listViewAdapter);
        listView.setDivider(null);
    }

    public ListView getListView() {
        return listView;
    }

    public void setListViewListener(ReloadListView.LoadListener loadListener) {
        listView.setLoadListener(loadListener);
    }


    public void loadComplete() {
        listViewAdapter.notifyDataSetChanged();
        listView.loadComplete();
        /*if (HGVedioView.player!=null){
            if (HGVedioView.player.isPlaying()){
                HGVedioView.player.stop();
            }
            HGVedioView.player.release();
            HGVedioView.player=null;
        }*/
    }
}
