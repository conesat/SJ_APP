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

    public HGNewsListView(Context context, List<HGNewsItemEntity> newsEntity) {
        this.context = context;
        this.newsEntity = newsEntity;
        initView();
    }

    private void initView() {
        listView=new ReloadListView(context);
        listView.setAdapter(new ListViewAdapter(context,newsEntity));
        listView.setDivider(null);
    }

    public ListView getListView(){
        return  listView;
    }

    public void setListViewListener(ReloadListView.LoadListener loadListener){
        listView.setInteface(loadListener);
    }

}
