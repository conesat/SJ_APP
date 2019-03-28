package com.hg.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hg.ui.R;
import com.hg.ui.entity.HGNewsItemEntity;
import com.hg.ui.hodler.RecyclerNewsListViewHolder;
import com.hg.ui.jcvideoplayer_lib.JCVideoPlayer;
import com.hg.ui.layout.HGImageRecyclerView;
import com.hg.ui.listener.OnNewsItemClickListener;
import com.hg.ui.tools.NumAbbreviation;
import com.hg.ui.view.GoodView;
import com.hg.ui.view.NewsItemView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerNewsListAdapter extends RecyclerView.Adapter<RecyclerNewsListViewHolder> {

    private Context context;

    private OnNewsItemClickListener onNewsItemClickListener;

    private GoodView mGoodView;

    public List<HGNewsItemEntity> getListItems() {
        return listItems;
    }

    public void setListItems(List<HGNewsItemEntity> listItems) {
        this.listItems = listItems;
    }

    private List<HGNewsItemEntity> listItems;

    public RecyclerNewsListAdapter(Context context, List<HGNewsItemEntity> listItems, OnNewsItemClickListener onNewsItemClickListener) {
        this.context = context;
        this.listItems = listItems;
        this.onNewsItemClickListener = onNewsItemClickListener;
        if (onNewsItemClickListener != null)
            mGoodView = new GoodView(context);
    }

    @NonNull
    @Override
    public RecyclerNewsListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        NewsItemView view = new NewsItemView(context);
        RecyclerNewsListViewHolder myHolder = new RecyclerNewsListViewHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerNewsListViewHolder myHolder, int i) {
        NewsItemView newsItem = (NewsItemView) myHolder.itemView;

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
        if (listItems.get(i).isRecommened()) {
            newsItem.recommendIcon.setImageResource(R.drawable.recommend_1);
        }else {
            newsItem.recommendIcon.setImageResource(R.drawable.recommend);
        }

        newsItem.sendTime.setText(listItems.get(i).getSendTime());
        newsItem.userName.setText(listItems.get(i).getSenderName());
        newsItem.newsDetail.setText(listItems.get(i).getNewsDetail());

        newsItem.commentText.setText(NumAbbreviation.getNumAbbreviation(listItems.get(i).getCommentNum()));
        newsItem.forwardText.setText(NumAbbreviation.getNumAbbreviation(listItems.get(i).getForwardNum()));
        newsItem.recommendText.setText(NumAbbreviation.getNumAbbreviation(listItems.get(i).getRecommendNum()));

        if (onNewsItemClickListener != null) {
            setOnNewsItemClickListener(onNewsItemClickListener, newsItem, listItems.get(i));
        }
    }


    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public void setOnNewsItemClickListener(final OnNewsItemClickListener onNewsItemClickListener, final NewsItemView newsItemView, final HGNewsItemEntity hgNewsItemEntity) {
        newsItemView.newsDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewsItemClickListener.onClick(newsItemView, OnNewsItemClickListener.NEWSDETAIL, hgNewsItemEntity);
            }
        });
        newsItemView.userIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewsItemClickListener.onClick(newsItemView, OnNewsItemClickListener.USERICON, hgNewsItemEntity);
            }
        });
        newsItemView.userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewsItemClickListener.onClick(newsItemView, OnNewsItemClickListener.USERNAME, hgNewsItemEntity);
            }
        });
        newsItemView.sendTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewsItemClickListener.onClick(newsItemView, OnNewsItemClickListener.SENDTIME, hgNewsItemEntity);
            }
        });
        newsItemView.medias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewsItemClickListener.onClick(newsItemView, OnNewsItemClickListener.MEDIAS, hgNewsItemEntity);
            }
        });
        newsItemView.recommendIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!hgNewsItemEntity.isRecommened()) {
                    hgNewsItemEntity.setRecommened(true);
                    newsItemView.recommendIcon.setImageResource(R.drawable.recommend_1);
                    mGoodView.setImage(context.getResources().getDrawable(R.drawable.recommend_2));
                    mGoodView.show(newsItemView.recommendIcon);
                } else {
                    hgNewsItemEntity.setRecommened(false);
                    newsItemView.recommendIcon.setImageResource(R.drawable.recommend);
                }
                onNewsItemClickListener.onClick(newsItemView, OnNewsItemClickListener.RECOMMENDICON, hgNewsItemEntity);
            }
        });
        newsItemView.forwardIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewsItemClickListener.onClick(newsItemView, OnNewsItemClickListener.FORWARDICON, hgNewsItemEntity);
            }
        });

        newsItemView.commentIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewsItemClickListener.onClick(newsItemView, OnNewsItemClickListener.COMMENTICON, hgNewsItemEntity);
            }
        });

        newsItemView.recommendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewsItemClickListener.onClick(newsItemView, OnNewsItemClickListener.RECOMMENDTEXT, hgNewsItemEntity);
            }
        });
        newsItemView.forwardText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewsItemClickListener.onClick(newsItemView, OnNewsItemClickListener.FORWARDTEXT, hgNewsItemEntity);
            }
        });
        newsItemView.forwardText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewsItemClickListener.onClick(newsItemView, OnNewsItemClickListener.FORWARDTEXT, hgNewsItemEntity);
            }
        });
        newsItemView.commentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewsItemClickListener.onClick(newsItemView, OnNewsItemClickListener.COMMENTTEXT, hgNewsItemEntity);
            }
        });
        if (newsItemView.jcVideoPlayer != null) {
            newsItemView.jcVideoPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNewsItemClickListener.onClick(newsItemView, OnNewsItemClickListener.JCVIDEOPLAYER, hgNewsItemEntity);
                }
            });
        }
        if (newsItemView.hgImageRecyclerView != null) {
            newsItemView.hgImageRecyclerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNewsItemClickListener.onClick(newsItemView, OnNewsItemClickListener.HGIMAGERECYCLERVIEW, hgNewsItemEntity);
                }
            });
        }
    }
}
