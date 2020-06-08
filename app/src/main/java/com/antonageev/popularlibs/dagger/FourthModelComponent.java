package com.antonageev.popularlibs.dagger;

import com.antonageev.popularlibs.models.FourthModel;

import dagger.Component;

@Component(modules = {DaggerNetModule.class})
public interface FourthModelComponent {
    void injectToFourthModel(FourthModel fourthModel);
}
