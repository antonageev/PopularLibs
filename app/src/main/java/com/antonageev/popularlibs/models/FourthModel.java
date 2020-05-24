package com.antonageev.popularlibs.models;

import android.util.Log;

import com.antonageev.popularlibs.IFourthPresenterCallBack;

import java.text.SimpleDateFormat;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class FourthModel {

    private final String TAG = FourthModel.class.getSimpleName();
    private IFourthPresenterCallBack iFourthPresenterCallBack;

    public FourthModel(IFourthPresenterCallBack iFourthPresenterCallBack) {
        this.iFourthPresenterCallBack = iFourthPresenterCallBack;
    }

    private List<GitHubUsers> listUsers;
    private String textDataAboutUser;

    public List<GitHubUsers> getListUsers() {
        return listUsers;
    }

    public String getTextDataAboutUser() {
        return textDataAboutUser;
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    Retrofit retrofit2 = new Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

    interface ApiGson {
        @GET("/users")
        Single<List<GitHubUsers>> loadUsers();
    }

    interface ApiScalar {
        @GET("/users/{user}")
        Single<String> loadSingleUser(@Path("user") String user);
    }

    public Disposable modelRequestSingleUser(String user) {
        ApiScalar apiScalar = retrofit2.create(ApiScalar.class);
        Single<String> single = apiScalar.loadSingleUser(user);
        return single.retry(2).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.wtf(TAG, "onSuccess: received SCALAR data from Retrofit");
                        textDataAboutUser = s;
                        iFourthPresenterCallBack.onSingleUserUpdated();
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.wtf(TAG, "onError: failed to load single user data with Retrofit");
                        e.printStackTrace();
                        iFourthPresenterCallBack.onModelUpdateFailed();
                    }
                });
    }

    public Disposable modelRequestUsers() {
        ApiGson apiGson = retrofit.create(ApiGson.class);
        Single<List<GitHubUsers>> single = apiGson.loadUsers();
        return single.retry(2).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<GitHubUsers>>() {
                    @Override
                    public void onSuccess(List<GitHubUsers> gitHubUsers) {
                        Log.wtf(TAG, "onSuccess: received JSON data from Retrofit");
                        listUsers = gitHubUsers;
                        iFourthPresenterCallBack.onModelUpdated();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.wtf(TAG, "onError: failed to load data with Retrofit");
                        e.printStackTrace();
                        iFourthPresenterCallBack.onModelUpdateFailed();
                    }
                });
    }
}
