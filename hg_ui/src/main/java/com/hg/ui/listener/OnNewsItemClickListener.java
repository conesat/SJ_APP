package com.hg.ui.listener;

import com.hg.ui.entity.HGNewsItemEntity;
import com.hg.ui.view.NewsItemView;

public abstract class OnNewsItemClickListener {

    public static int USERICON=1;//用户头像
    public static int USERNAME=2;//用户姓名
    public static int SENDTIME=3;//发送时间
    public static int NEWSDETAIL=4;//详细标题
    public static int MEDIAS=5;//媒体框 包含图片与视频
    public static int RECOMMENDICON=6;//推荐按钮
    public static int FORWARDICON=7;//转发按钮
    public static int COMMENTICON=8;//评论按钮
    public static int RECOMMENDTEXT=9;//推荐文字
    public static int FORWARDTEXT=10;//转发文字
    public static int COMMENTTEXT=11;//评论文字
    public static int JCVIDEOPLAYER=12;//视频框
    public static int HGIMAGERECYCLERVIEW=13;//图片框

    private int clickItem;

    public int getClickItem() {
        return clickItem;
    }

    public void setClickItem(int clickItem) {
        this.clickItem = clickItem;
    }

    /*    public CircularImageView userIcon;
        public TextView userName;
        public TextView sendTime;
        public TextView nesDetail;
        public LinearLayout medias;
        public ImageView recommendIcon;
        public ImageView forwardIcon;
        public ImageView commentIcon;
        public TextView recommendText;
        public TextView forwardText;
        public TextView commentText;
        private JCVideoPlayer jcVideoPlayer;
        private HGImageRecyclerView hgImageRecyclerView;*/

    /**
     * @param newsItemView 点击item对象
     * @param clickItem 具体点击的内容
     * @param hgNewsItemEntity 内容
     */
    public abstract void onClick(NewsItemView newsItemView, int clickItem, HGNewsItemEntity hgNewsItemEntity);
}
