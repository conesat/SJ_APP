package com.hg.ui.layout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hg.ui.adapter.RecyclerViewAdapter;
import com.hg.ui.entity.HGNewsMediaEntity;

import java.util.List;

public class HGImageRecyclerView {
    private RecyclerView recyclerView;
    private Context context;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<HGNewsMediaEntity> pictureEntities;

    public HGImageRecyclerView(Context context, List<HGNewsMediaEntity> pictureEntities) {
        this.context = context;
        this.pictureEntities = pictureEntities;
        initView();
    }

    private void initView() {
        recyclerView=new RecyclerView(context);
        recyclerViewAdapter = new RecyclerViewAdapter(context, pictureEntities);
        recyclerView.setAdapter(recyclerViewAdapter);
        if (pictureEntities.size()<4) {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(pictureEntities.size(), StaggeredGridLayoutManager.VERTICAL));
        }else {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        }
        ViewGroup.LayoutParams param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutParams(param);
    }

    public View getView(){
        return recyclerView;
    }


}
