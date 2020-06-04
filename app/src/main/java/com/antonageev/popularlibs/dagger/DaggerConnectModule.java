package com.antonageev.popularlibs.dagger;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import dagger.Module;
import dagger.Provides;

@Module
public class DaggerConnectModule {

    @Provides
    NetworkInfo getNetworkInfo(ConnectivityManager cm) {
        if (cm == null) return null;
        return cm.getActiveNetworkInfo();
    }

    @Provides
    ConnectivityManager getConnectivityManager(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }
}
