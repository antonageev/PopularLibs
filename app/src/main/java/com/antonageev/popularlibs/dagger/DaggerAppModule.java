package com.antonageev.popularlibs.dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class DaggerAppModule {

    private Context contextApp;

    public DaggerAppModule(Context context) {
        this.contextApp = context;
    }

    @Provides
    Context provideContext() {
        return contextApp;
    }
}
