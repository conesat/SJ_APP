package com.hg.ui.hodler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.hg.ui.view.SquareImageView;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    public SquareImageView imageView;
    public RecyclerViewHolder(@NonNull SquareImageView itemView) {
        super(itemView);
        this.imageView=itemView;
    }

}