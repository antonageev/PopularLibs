package com.antonageev.popularlibs;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.antonageev.popularlibs.databases.GitDatabase;
import com.antonageev.popularlibs.databases.GitHubUsersDao;

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();
    private GitDatabase db;

    private static App instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Log.wtf(TAG, "onCreate: instance = " + instance);
        db = Room.databaseBuilder(getApplicationContext(), GitDatabase.class, "git_users")
                .build();
    }

    public GitHubUsersDao getGitHubUsersDao() {
        return db.getGitHubUsersDao();
    }


    public static App getInstance() {
        return instance;
    }

}
