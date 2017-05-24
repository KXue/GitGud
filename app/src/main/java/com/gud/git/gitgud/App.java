package com.gud.git.gitgud;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Created by KevinXue on 5/16/2017.
 */

public class App extends Application {
    private static Application sApplication;
    private static DisplayMetrics sMetrics;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
    public static int getScreenWidth() {
        return sMetrics.widthPixels;
    }
    public static int getScreenHeight() {
        return sMetrics.heightPixels;
    }

    public static void setDisplayMetrics(DisplayMetrics metrics){
        sMetrics = metrics;
        Log.d("App", "Width: " + getScreenWidth());
        Log.d("App", "Height: " + getScreenHeight());
    }
}
