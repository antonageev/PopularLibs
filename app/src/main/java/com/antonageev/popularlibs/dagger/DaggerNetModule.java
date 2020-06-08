package com.antonageev.popularlibs.dagger;

import android.os.Bundle;

import com.antonageev.popularlibs.databases.SugarModel;
import com.antonageev.popularlibs.models.GitHubUsers;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DaggerNetModule {

    @Provides
    public String provideEndPoint() {
        return "https://api.github.com";
    }


    @Provides
    Retrofit getRetrofit(String endPoint) {
        return new Retrofit.Builder()
                .baseUrl(endPoint)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    Single<List<GitHubUsers>> getSingle(Retrofit retrofit) {
        ApiGson apiGson = retrofit.create(ApiGson.class);
        return apiGson.loadUsers();
    }

}
