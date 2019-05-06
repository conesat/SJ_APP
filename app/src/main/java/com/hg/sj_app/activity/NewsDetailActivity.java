package com.hg.sj_app.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hg.sj_app.R;
import com.hg.sj_app.values.StaticValues;
import com.hg.ui.entity.HGNewsItemEntity;
import com.hg.ui.jcvideoplayer_lib.JCVideoPlayer;
import com.hg.ui.layout.HGImageRecyclerView;
import com.hg.ui.view.CircularImageView;
import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {

    private HGNewsItemEntity hgNewsItemEntity;

    private CircularImageView userImg;

    private TextView userName;
    private TextView detail;
    private LinearLayout mediaLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
            getWindow().setAttributes(lp);
            //全屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            //无title
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //全屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_news_detail);
        hgNewsItemEntity = StaticValues.hgNewsItemEntity;
        initView();
    }

    private void initView() {
        userImg = findViewById(R.id.hg_news_user_icon);
        userName = findViewById(R.id.hg_news_user_name);
        detail = findViewById(R.id.hg_news_detail);
        mediaLayout = findViewById(R.id.hg_news_medias);

        detail.setText(hgNewsItemEntity.getNewsDetail());
        userName.setText(hgNewsItemEntity.getSenderName());
        Picasso.with(this).load(hgNewsItemEntity.getSenderIconUrl()).into(userImg);
        mediaLayout.removeAllViews();
        if (StaticValues.hgNewsItemEntity.getMedias() != null && StaticValues.hgNewsItemEntity.getMedias().size() > 0) {
            if (StaticValues.hgNewsItemEntity.getMediaType().equals("vedio")) {
                JCVideoPlayer jcVideoPlayer = new JCVideoPlayer(NewsDetailActivity.this, null);
                jcVideoPlayer.setUp(
                        StaticValues.hgNewsItemEntity.getMedias().get(0).getSourceImgUrl(),
                        StaticValues.hgNewsItemEntity.getMedias().get(0).getReviewImgUrl(),
                        null);

                mediaLayout.addView(jcVideoPlayer);
            } else if (StaticValues.hgNewsItemEntity.getMediaType().equals("img")) {
                HGImageRecyclerView hgImageRecyclerView = new HGImageRecyclerView(NewsDetailActivity.this);
                mediaLayout.addView(hgImageRecyclerView);
                hgImageRecyclerView.loadData(StaticValues.hgNewsItemEntity.getMedias());
            }
        }
    }

    public void back(View view) {
        this.finish();
    }
}
