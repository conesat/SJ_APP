package com.hg.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hg.ui.entity.HGNewsMediaEntity;
import com.hg.ui.hodler.RecyclerViewHolder;
import com.hg.ui.view.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<HGNewsMediaEntity> pictureEntities;

    public RecyclerViewAdapter(Context context, List<HGNewsMediaEntity> pictureEntities) {
        this.context = context;
        this.pictureEntities = pictureEntities;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i >= 6)
            return null;
        SquareImageView view = new SquareImageView(context);
        view.setPadding(2, 2, 2, 2);
        RecyclerViewHolder myHolder = new RecyclerViewHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder myHolder, final int i) {
        Picasso.with(context).load(pictureEntities.get(i).getReviewImgUrl()).into(myHolder.imageView);
      /*  myHolder.roundImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        myHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
    }


    @Override
    public int getItemCount() {
        return pictureEntities.size();
    }
}
