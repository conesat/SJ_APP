package com.hg.ui.layout;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hg.ui.adapter.RecyclerViewAdapter;
import com.hg.ui.entity.HGNewsMediaEntity;

import java.util.List;

public class HGImageRecyclerView extends RecyclerView {
    //  private RecyclerView recyclerView;
    private Context context;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<HGNewsMediaEntity> pictureEntities;

    public HGImageRecyclerView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public void loadData(final List<HGNewsMediaEntity> pictureEntities) {

        if (pictureEntities.size()>9) {
            this.pictureEntities = pictureEntities.subList(0, 9);
        }else {
            this.pictureEntities = pictureEntities;
        }

        recyclerViewAdapter = new RecyclerViewAdapter(context, this.pictureEntities);
        setAdapter(recyclerViewAdapter);

        final GridLayoutManager manager = new GridLayoutManager(getContext(), 6);

        setLayoutManager(manager);

        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (pictureEntities.size() > 0) {
                    if (pictureEntities.size() <= 3) {
                        return manager.getSpanCount() / pictureEntities.size();
                    } else if (pictureEntities.size() == 4) {
                        return 3;
                    } else if (pictureEntities.size() == 5 && position < 2) {
                        return 3;
                    } else if (pictureEntities.size() == 5 && position >= 2) {
                        return 2;
                    } else if (pictureEntities.size() == 7 && position < 3) {
                        return 2;
                    } else if (pictureEntities.size() == 7 && position >= 3) {
                        return 3;
                    } else if (pictureEntities.size() == 8 && position < 6) {
                        return 2;
                    } else if (pictureEntities.size() == 8 && position >= 6) {
                        return 3;
                    }
                }
                return 2;// manager.getSpanCount();
            }

        });
    }

    private void initView() {
        ViewGroup.LayoutParams param = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(param);
    }

}
