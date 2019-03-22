package com.hg.ui.transformer.builder;

import android.support.v4.view.ViewPager;

import com.hg.ui.transformer.AccordionTransformer;
import com.hg.ui.transformer.BackgroundToForegroundTransformer;
import com.hg.ui.transformer.CubeInTransformer;
import com.hg.ui.transformer.CubeOutTransformer;
import com.hg.ui.transformer.DepthPageTransformer;
import com.hg.ui.transformer.FlipHorizontalTransformer;
import com.hg.ui.transformer.FlipVerticalTransformer;
import com.hg.ui.transformer.ForegroundToBackgroundTransformer;
import com.hg.ui.transformer.RotateDownTransformer;
import com.hg.ui.transformer.RotateUpTransformer;
import com.hg.ui.transformer.ScaleInOutTransformer;
import com.hg.ui.transformer.StackTransformer;
import com.hg.ui.transformer.TabletTransformer;
import com.hg.ui.transformer.ZoomInTransformer;
import com.hg.ui.transformer.ZoomOutSlideTransformer;
import com.hg.ui.transformer.ZoomOutTranformer;

public class TransformerBuilder {
    public static ViewPager setViewPagerTransformer(ViewPager viewPager, int i) {
        switch (i) {
            case 0:
                viewPager.setPageTransformer(true,
                        new AccordionTransformer());
                break;
            case 1:
                viewPager.setPageTransformer(true,
                        new BackgroundToForegroundTransformer());

                break;
            case 2:
                viewPager.setPageTransformer(true,
                        new CubeInTransformer());
                break;
            case 3:
                viewPager.setPageTransformer(true,
                        new CubeOutTransformer());
                break;
            case 4:
                viewPager.setPageTransformer(true,
                        new DepthPageTransformer());
                break;
            case 5:
                viewPager.setPageTransformer(true,
                        new FlipHorizontalTransformer());
                break;
            case 6:
                viewPager.setPageTransformer(true,
                        new FlipVerticalTransformer());
                break;
            case 7:
                viewPager.setPageTransformer(true,
                        new ForegroundToBackgroundTransformer());
                break;
            case 8:
                viewPager.setPageTransformer(true,
                        new RotateDownTransformer());
                break;
            case 9:
                viewPager.setPageTransformer(true,
                        new ScaleInOutTransformer());
                break;
            case 10:
                viewPager.setPageTransformer(true,
                        new StackTransformer());
                break;
            case 11:
                viewPager.setPageTransformer(true,
                        new TabletTransformer());
                break;
            case 12:
                viewPager.setPageTransformer(true,
                        new ZoomInTransformer());
                break;
            case 13:
                viewPager.setPageTransformer(true,
                        new ZoomOutSlideTransformer());
                break;
            case 14:
                viewPager.setPageTransformer(true,
                        new ZoomOutTranformer());
                break;
            case 15:
                viewPager.setPageTransformer(true,
                        new RotateUpTransformer());
                break;
            default:
                break;
        }
        return viewPager;
    }
}
