package com.example.kimjs.sample.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.kimjs.sample.R;
import com.example.kimjs.sample.artifacts.WeatherLocationInfo;
import com.example.kimjs.sample.database.DBManager;
import com.example.kimjs.sample.infoparser.WeatherParser;

import java.util.ArrayList;

public class LoadingActivity extends Activity {

    private Context mContext;
    private Thread mThread;
    private String TAG = "LoadingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mContext = getApplicationContext();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mThread = new Thread(loading);
        mThread.start();
    }

    private Runnable loading = new Runnable() {
        @Override
        public void run() {
            if(DBManager.getManager(mContext).isSetWeatherLocation()) {
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }
            else {
                WeatherParser wp = new WeatherParser();
                ArrayList<WeatherLocationInfo> array_location;
                array_location = wp.getAllLocationInfo();
                DBManager.getManager(mContext).setWeatherLocationAll(array_location);
                mHandler.sendEmptyMessage(0);
            }
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0) {
                mThread.interrupt();
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };
}
