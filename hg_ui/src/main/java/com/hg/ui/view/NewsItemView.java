package com.hg.ui.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hg.ui.R;

public class NewsItemView extends LinearLayout {
    public CircularImageView userIcon;
    public TextView userName;
    public TextView sendTime;
    public TextView nesDetail;
    public RecyclerView medias;
    public ImageView recommendIcon;
    public ImageView forwardIcon;
    public ImageView commentIcon;
    public TextView recommendText;
    public TextView forwardText;
    public TextView commentText;

    public NewsItemView(Context context) {
        super(context);
        View inflate = inflate(getContext(), R.layout.hg_news_item, this);
        userIcon = inflate.findViewById(R.id.hg_news_user_icon);
        userName = inflate.findViewById(R.id.hg_news_user_name);
        sendTime = inflate.findViewById(R.id.hg_news_send_time);
        nesDetail = inflate.findViewById(R.id.hg_news_detail);
        medias = inflate.findViewById(R.id.hg_news_medias);
        recommendIcon = inflate.findViewById(R.id.hg_news_recommend_icon);
        forwardIcon = inflate.findViewById(R.id.hg_news_forward_icon);
        commentIcon = inflate.findViewById(R.id.hg_news_comment_icon);
        recommendText = inflate.findViewById(R.id.hg_news_recommend_text);
        forwardText = inflate.findViewById(R.id.hg_news_forward_text);
        commentText = inflate.findViewById(R.id.hg_news_comment_text);
        nesDetail.setMaxLines(4);
        nesDetail.setEllipsize(TextUtils.TruncateAt.END);
    }
}
