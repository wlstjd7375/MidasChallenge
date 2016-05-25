package com.example.kimjs.sample.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import com.example.kimjs.sample.R;
import com.example.kimjs.sample.globalmanager.GlobalVariable;

import java.lang.reflect.Field;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    public CustomLayoutSet layout;
    public static final int SIGNAL_UI_UPDATE = 0x10001001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        layout = new CustomLayoutSet(this,MainActivity.this);
        layout.setAnimationDuration(400);
        layout.setInterpolator(new AccelerateDecelerateInterpolator());
        setContentView(layout);

        getDpi(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        layout.getHandler().sendEmptyMessage(SIGNAL_UI_UPDATE);
    }

    public void getDpi(Context context)
    {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        Log.i(TAG, "density :" + metrics.density);
        // density interms of dpi
        Log.i(TAG, "D density :" + metrics.densityDpi);
        // horizontal pixel resolution
        Log.i(TAG, "width pix :" + metrics.widthPixels);
        // actual horizontal dpi
        Log.i(TAG, "xdpi :" + metrics.xdpi);
        // actual vertical dpi
        Log.i(TAG, "ydpi :" +  metrics.ydpi);

        GlobalVariable.GLOBAL_DPI = metrics.densityDpi;
    }
}
