package com.zhulinyin.demo;

import android.app.Application;

import com.zhulinyin.multiprocess_eventbus.MultiProcessEventBus;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiProcessEventBus.getDefault().init(this);
    }
}
