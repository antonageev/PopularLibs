package com.antonageev.popularlibs.models;

import android.os.Bundle;
import android.util.Log;

import com.antonageev.popularlibs.App;
import com.antonageev.popularlibs.Converter;
import com.antonageev.popularlibs.IFourthPresenterCallBack;
import com.antonageev.popularlibs.databases.SugarModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class FourthModel {

    @Inject
    Retrofit retrofit;

    @Inject
    Single<List<GitHubUsers>> single;

    private final String TAG = FourthModel.class.getSimpleName();
    private IFourthPresenterCallBack iFourthPresenterCallBack;
    private PublishSubject<String> subject;

    public FourthModel(IFourthPresenterCallBack iFourthPresenterCallBack) {
        this.iFourthPresenterCallBack = iFourthPresenterCallBack;
        subject = PublishSubject.create();
        App.getFourthModelComponent().injectToFourthModel(this);
    }

    private List<GitHubUsers> listUsers;
    private String textDataAboutUser;
    private String resultText = "";

    public PublishSubject getSubject() {
        return subject;
    }

    public String getResultText() {
        return resultText;
    }

    public String setResultText(String resultText) {
        this.resultText = resultText;
        return this.resultText;
    }

    public List<GitHubUsers> getListUsers() {
        return listUsers;
    }

    public String getTextDataAboutUser() {
        return textDataAboutUser;
    }

    Retrofit retrofit2 = new Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

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

    private DisposableSingleObserver<Bundle> createSingleObserver() {
        return new DisposableSingleObserver<Bundle>() {
            @Override
            public void onSuccess(Bundle bundle) {
                resultText = getSbFromBundle(bundle).toString();
                subject.onNext(resultText);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        };
    }

    private DisposableSingleObserver<Bundle> createSingleObserverAndUpdateModel() {
        return new DisposableSingleObserver<Bundle>() {
            @Override
            public void onSuccess(Bundle bundle) {
                resultText = getSbFromBundle(bundle).toString();
                subject.onNext(resultText);
                iFourthPresenterCallBack.onModelUpdated();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        };
    }

    private StringBuilder getSbFromBundle(Bundle bundle) {
        return new StringBuilder()
                .append("Row number = ")
                .append(bundle.getInt("count"))
                .append("\n")
                .append("Time in milliseconds = ")
                .append(bundle.getLong("millis"));
    }

    public Disposable uploadToSugarDB() {
        Single<Bundle> uploadSugarSingle = Single.create(new SingleOnSubscribe<Bundle>() {
            @Override
            public void subscribe(SingleEmitter<Bundle> e) throws Exception {
                long start = System.currentTimeMillis();
                for (GitHubUsers item : listUsers) {
                    SugarModel sugarModel = new SugarModel(item.getLogin(), item.getAvatarUrl(), item.getUrl());
                    sugarModel.save();
                }
                long timeLapse = System.currentTimeMillis() - start;
                int size = SugarModel.listAll(SugarModel.class).size();
                Bundle bundle = new Bundle();
                bundle.putInt("count", size);
                bundle.putLong("millis", timeLapse);
                e.onSuccess(bundle);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return uploadSugarSingle.subscribeWith(createSingleObserver());
    }

    public Disposable uploadToRoomDB() {
        Single<Bundle> uploadRoomSingle = Single.create(new SingleOnSubscribe<Bundle>() {
            @Override
            public void subscribe(SingleEmitter<Bundle> e) throws Exception {
                long start = System.currentTimeMillis();
                for (GitHubUsers item : listUsers) {
                    GitHubUsers user = new GitHubUsers(item.getLogin(), item.getAvatarUrl(), item.getUrl());
                    App.getInstance().getGitHubUsersDao().insertItem(user);
                }
//                App.getInstance().getGitHubUsersDao().insertAll(listUsers);
                long timeLapse = System.currentTimeMillis() - start;
                int size = App.getInstance().getGitHubUsersDao().listAll().size();
                Bundle bundle = new Bundle();
                bundle.putLong("millis", timeLapse);
                bundle.putInt("count", size);
                e.onSuccess(bundle);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return uploadRoomSingle.subscribeWith(createSingleObserver());
    }


    public Disposable deleteFromSugarDB() {
        Single<Bundle> deleteSugarSingle = Single.create(new SingleOnSubscribe<Bundle>() {
            @Override
            public void subscribe(SingleEmitter<Bundle> e) throws Exception {
                long start = System.currentTimeMillis();
                SugarModel.deleteAll(SugarModel.class);
                long timeLapse = System.currentTimeMillis() - start;
                int size = SugarModel.listAll(SugarModel.class).size();
                Bundle bundle = new Bundle();
                bundle.putInt("count", size);
                bundle.putLong("millis", timeLapse);
                e.onSuccess(bundle);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return deleteSugarSingle.subscribeWith(createSingleObserver());
    }

    public Disposable deleteFromRoomDB() {
        Single<Bundle> deleteRoomSingle = Single.create(new SingleOnSubscribe<Bundle>() {
            @Override
            public void subscribe(SingleEmitter<Bundle> e) throws Exception {
                long start = System.currentTimeMillis();
                App.getInstance().getGitHubUsersDao().deleteAll();
                long timeLapse = System.currentTimeMillis() - start;
                int size = App.getInstance().getGitHubUsersDao().listAll().size();
                Bundle bundle = new Bundle();
                bundle.putInt("count", size);
                bundle.putLong("millis", timeLapse);
                e.onSuccess(bundle);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return deleteRoomSingle.subscribeWith(createSingleObserver());
    }

    public Disposable getFromSugarDB() {
        Single<Bundle> getSugarSingle = Single.create(new SingleOnSubscribe<Bundle>() {
            @Override
            public void subscribe(SingleEmitter<Bundle> e) throws Exception {
                long start = System.currentTimeMillis();
                List<SugarModel> sugarList = SugarModel.listAll(SugarModel.class);
                long timeLapse = System.currentTimeMillis() - start;
                listUsers = Converter.convertToGitHubUsersFromSugar(sugarList);  //как проапдейтить адаптер данными из БД- подумать...
                int size = sugarList.size();
                Bundle bundle = new Bundle();
                bundle.putInt("count", size);
                bundle.putLong("millis", timeLapse);
                e.onSuccess(bundle);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return getSugarSingle.subscribeWith(createSingleObserverAndUpdateModel());
    }

    public Disposable getFromRoomDB() {
        Single<Bundle> getRoomSingle = Single.create(new SingleOnSubscribe<Bundle>() {
            @Override
            public void subscribe(SingleEmitter<Bundle> e) throws Exception {
                long start = System.currentTimeMillis();
                listUsers = App.getInstance().getGitHubUsersDao().listAll();
                long timeLapse = System.currentTimeMillis() - start;
                int size = App.getInstance().getGitHubUsersDao().listAll().size();
                Bundle bundle = new Bundle();
                bundle.putInt("count", size);
                bundle.putLong("millis", timeLapse);
                e.onSuccess(bundle);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        return getRoomSingle.subscribeWith(createSingleObserverAndUpdateModel());
    }

}
