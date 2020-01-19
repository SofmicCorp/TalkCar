package com.example.talkcar;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class Effects {

    public void shine(ImageView img, ImageView shine){

        Animation animation = new TranslateAnimation(0, img.getWidth()+ shine.getWidth(),0, 0);
        animation.setDuration(550);
        animation.setFillAfter(false);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        shine.startAnimation(animation);
    }
}
