package com.gud.git.gitgud;

import android.app.Application;
import android.content.Context;

/**
 * Created by KevinXue on 5/16/2017.
 */

public class App extends Application {
    private static Application sApplication;

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
        return getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }
}
