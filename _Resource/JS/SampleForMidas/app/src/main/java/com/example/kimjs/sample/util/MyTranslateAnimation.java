package com.example.kimjs.sample.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MyTranslateAnimation extends Animation {

    private View mView;
    private final float fromX;
    private final float toX;
    private final float fromY;
    private final float toY;

    public MyTranslateAnimation(View v, float fromX, float toX, float fromY, float toY) {

        mView = v;
        this.fromX = fromX;
        this.toX = toX;
        this.fromY = fromY;
        this.toY = toY;
        setDuration(800);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float x =
                (toX - fromX) * interpolatedTime + fromX;
        float y = (toY - fromY) * interpolatedTime + fromY;

        if(mView.getClass().getName().equalsIgnoreCase("android.widget.RelativeLayout"))
        {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mView.getLayoutParams();
            params.setMargins((int) x, (int) y, 0, 0);
        }
        else if(mView.getClass().getName().equalsIgnoreCase("android.widget.FrameLayout"))
        {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mView.getLayoutParams();
            params.setMargins((int) x, (int) y, 0, 0);
        }
        else if(mView.getClass().getName().equalsIgnoreCase("android.widget.LinearLayout"))
        {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mView.getLayoutParams();
            params.setMargins((int) x, (int) y, 0, 0);
        }
        else if(mView.getClass().getName().equalsIgnoreCase("android.widget.ImageView"))
        {

        }
        //LayoutParams params = (LayoutParams) mView.getLayoutParams();
//        mView.setY(y);
//        mView.setX(x);
        mView.requestLayout();
    }
}