package com.hg.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hg.ui.entity.HGNewsItemEntity;
import com.hg.ui.tools.NumAbbreviation;
import com.hg.ui.view.NewsItemView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private List<HGNewsItemEntity> listItems;

    private Context context;

    public ListViewAdapter(Context context, List<HGNewsItemEntity> listItems) {
        this.context = context;
        if (listItems==null){
            this.listItems=new ArrayList<>();
        }else {
            this.listItems = listItems;
        }
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsItemView newsItem;
        if (convertView == null) {
            newsItem = new NewsItemView(context);
            //设置控件集到convertView
            convertView=newsItem;
            convertView.setTag(newsItem);
        } else {
            newsItem = (NewsItemView) convertView.getTag();
        }

        Picasso.with(context).load(listItems.get(position).getSenderIconUrl()).into(newsItem.userIcon);

        newsItem.sendTime.setText(listItems.get(position).getSendTime());
        newsItem.userName.setText(listItems.get(position).getSenderName());
        newsItem.nesDetail.setText(listItems.get(position).getNewsDetail());

        newsItem.commentText.setText(NumAbbreviation.getNumAbbreviation(listItems.get(position).getCommentNum()));
        newsItem.forwardText.setText(NumAbbreviation.getNumAbbreviation(listItems.get(position).getForwardNum()));
        newsItem.recommendText.setText(NumAbbreviation.getNumAbbreviation(listItems.get(position).getRecommendNum()));

        return convertView;
    }


}