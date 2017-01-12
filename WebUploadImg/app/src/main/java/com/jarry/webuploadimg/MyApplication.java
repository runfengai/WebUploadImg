package com.jarry.webuploadimg;

import android.app.Application;

/**
 * Created by zhengxr on 2017/1/12.
 */

public class MyApplication extends Application {
    public static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

}
