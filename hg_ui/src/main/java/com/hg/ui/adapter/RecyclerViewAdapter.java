package com.hg.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hg.ui.entity.HGNewsMediaEntity;
import com.hg.ui.hodler.RecyclerViewHolder;
import com.hg.ui.view.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private Context context;
    private List<HGNewsMediaEntity> pictureEntities;

    public RecyclerViewAdapter(Context context, List<HGNewsMediaEntity> pictureEntities) {
        this.context = context;
        this.pictureEntities = pictureEntities;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i >= 9)
            return null;
        SquareImageView view = new SquareImageView(context);
        view.setPadding(2, 2, 2, 2);
        RecyclerViewHolder myHolder = new RecyclerViewHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder myHolder, final int i) {
        if (i < 9){
            if (pictureEntities.size() == 1) {
                Picasso.with(context)
                        .load(pictureEntities.get(i).getReviewImgUrl())
                        .into(myHolder.imageView);
            } else {
                Picasso.with(context)
                        .load(pictureEntities.get(i).getReviewImgUrl())
                        .resize(200, 200)
                        .centerCrop()
                        .into(myHolder.imageView);
            }
        }
        myHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, ShowPictureActivity.class);
                intent.putExtra("path", pictureEntities.get(i).getReviewImgUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);*/
            }
        });
    }


    @Override
    public int getItemCount() {
        return pictureEntities.size();
    }
}
