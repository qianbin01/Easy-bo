package com.qb.easy_bo;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;


public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FileDownloader.init(getApplicationContext());
    }
}
