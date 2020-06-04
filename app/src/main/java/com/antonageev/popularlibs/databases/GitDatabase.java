package com.antonageev.popularlibs.databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.antonageev.popularlibs.models.GitHubUsers;

@Database(entities = {GitHubUsers.class}, version = 1)
public abstract class GitDatabase extends RoomDatabase {
    public abstract GitHubUsersDao getGitHubUsersDao();
}
