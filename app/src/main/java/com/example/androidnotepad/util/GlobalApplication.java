package com.example.androidnotepad.util;

import android.app.Application;
import android.content.Context;

public class GlobalApplication extends Application {
    private static GlobalApplication instance;

    public static GlobalApplication getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
        // or return instance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
