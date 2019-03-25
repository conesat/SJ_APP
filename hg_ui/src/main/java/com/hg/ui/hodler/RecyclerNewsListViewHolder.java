package com.hg.ui.hodler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.hg.ui.view.NewsItemView;

public class RecyclerNewsListViewHolder extends RecyclerView.ViewHolder {
    public NewsItemView getNewsItem() {
        return newsItem;
    }

    public void setNewsItem(NewsItemView newsItem) {
        this.newsItem = newsItem;
    }

    private NewsItemView newsItem;

    public RecyclerNewsListViewHolder(@NonNull NewsItemView newsItem) {
        super(newsItem);
        this.newsItem=newsItem;
    }

}