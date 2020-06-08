package com.antonageev.popularlibs.dagger;

import com.antonageev.popularlibs.FourthActivity;

import dagger.Component;

@Component(modules = {DaggerConnectModule.class, DaggerAppModule.class})
public interface AppComponent {
    void injectToFourthActivity(FourthActivity fourthActivity);
}
