package com.hg.ui.animator;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class HGRotateAnimation {
    private ObjectAnimator objectAnimator;

    public HGRotateAnimation(View view, boolean autoStart, Integer time, boolean restart) {
        this(view, autoStart, time, restart, 0f, 360f);
    }


    @SuppressLint({"ObjectAnimatorBinding", "WrongConstant"})
    public HGRotateAnimation(View view, boolean autoStart, Integer time, boolean restart, float fromAngle, float toAngle) {
        if (time == null || time < 0) {
            time = 3000;
        }
        objectAnimator = ObjectAnimator.ofFloat(view, "rotation", fromAngle, toAngle);//添加旋转动画，旋转中心默认为控件中点
        objectAnimator.setDuration(time);//设置动画时间
        objectAnimator.setInterpolator(new LinearInterpolator());//动画时间线性渐变
        if (restart) {
            objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            objectAnimator.setRepeatMode(ObjectAnimator.RESTART);

        }
        if (autoStart) {
            start();
        }
    }


    public void start() {
        if (!objectAnimator.isRunning())
            objectAnimator.start();//动画开始
    }

    public void stop() {
        objectAnimator.end();//动画结束
    }

    public void pasue() {
        objectAnimator.pause();//动画暂停
    }
}
