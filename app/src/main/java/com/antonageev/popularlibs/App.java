package com.antonageev.popularlibs;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.antonageev.popularlibs.dagger.AppComponent;
import com.antonageev.popularlibs.dagger.DaggerAppComponent;
import com.antonageev.popularlibs.dagger.DaggerAppModule;
import com.antonageev.popularlibs.dagger.DaggerConnectModule;
import com.antonageev.popularlibs.dagger.DaggerFourthModelComponent;
import com.antonageev.popularlibs.dagger.DaggerNetModule;
import com.antonageev.popularlibs.dagger.FourthModelComponent;
import com.antonageev.popularlibs.databases.GitDatabase;
import com.antonageev.popularlibs.databases.GitHubUsersDao;

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();
    private GitDatabase db;

    private static App instance;

    private static AppComponent appComponent;
    private static FourthModelComponent fourthModelComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Log.wtf(TAG, "onCreate: instance = " + instance);
        db = Room.databaseBuilder(getApplicationContext(), GitDatabase.class, "git_users")
                .build();

        appComponent = DaggerAppComponent.builder()
                .daggerConnectModule(new DaggerConnectModule())
                .daggerAppModule(new DaggerAppModule(this))
                .build();

        fourthModelComponent = DaggerFourthModelComponent.builder()
                .daggerNetModule(new DaggerNetModule())
                .build();

    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public static FourthModelComponent getFourthModelComponent() {
        return fourthModelComponent;
    }

    public GitHubUsersDao getGitHubUsersDao() {
        return db.getGitHubUsersDao();
    }


    public static App getInstance() {
        return instance;
    }

}
