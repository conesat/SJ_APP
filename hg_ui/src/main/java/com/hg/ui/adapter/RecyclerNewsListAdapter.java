package com.hg.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hg.ui.entity.HGNewsItemEntity;
import com.hg.ui.hodler.RecyclerNewsListViewHolder;
import com.hg.ui.jcvideoplayer_lib.JCVideoPlayer;
import com.hg.ui.layout.HGImageRecyclerView;
import com.hg.ui.tools.NumAbbreviation;
import com.hg.ui.view.NewsItemView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerNewsListAdapter extends RecyclerView.Adapter<RecyclerNewsListViewHolder> {

    private Context context;

    public List<HGNewsItemEntity> getListItems() {
        return listItems;
    }

    public void setListItems(List<HGNewsItemEntity> listItems) {
        this.listItems = listItems;
    }

    private List<HGNewsItemEntity> listItems;

    public RecyclerNewsListAdapter(Context context, List<HGNewsItemEntity> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public RecyclerNewsListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i >= 8)
            return null;
        NewsItemView view = new NewsItemView(context);
        RecyclerNewsListViewHolder myHolder = new RecyclerNewsListViewHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerNewsListViewHolder myHolder, int i) {
        NewsItemView newsItem = (NewsItemView)myHolder.itemView;

        Picasso.with(context).load(listItems.get(i).getSenderIconUrl()).into(newsItem.userIcon);
        if (listItems.get(i).getMedias() != null && listItems.get(i).getMedias().size() > 0) {
            if (listItems.get(i).getMediaType().equals("vedio")) {
                JCVideoPlayer jcVideoPlayer = new JCVideoPlayer(context, null);
                jcVideoPlayer.setUp(
                        listItems.get(i).getMedias().get(0).getSourceImgUrl(),
                        listItems.get(i).getMedias().get(0).getReviewImgUrl(),
                        null);
                newsItem.setJcVideoPlayer(jcVideoPlayer);
            } else if (listItems.get(i).getMediaType().equals("img")) {
                HGImageRecyclerView hgImageRecyclerView = new HGImageRecyclerView(context);
                newsItem.setHgImageRecyclerView(hgImageRecyclerView);
                hgImageRecyclerView.loadData(listItems.get(i).getMedias());
            }
        }

        newsItem.sendTime.setText(listItems.get(i).getSendTime());
        newsItem.userName.setText(listItems.get(i).getSenderName());
        newsItem.nesDetail.setText(listItems.get(i).getNewsDetail());

        newsItem.commentText.setText(NumAbbreviation.getNumAbbreviation(listItems.get(i).getCommentNum()));
        newsItem.forwardText.setText(NumAbbreviation.getNumAbbreviation(listItems.get(i).getForwardNum()));
        newsItem.recommendText.setText(NumAbbreviation.getNumAbbreviation(listItems.get(i).getRecommendNum()));
    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }
}
