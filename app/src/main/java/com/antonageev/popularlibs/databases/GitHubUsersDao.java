package com.antonageev.popularlibs.databases;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.antonageev.popularlibs.models.GitHubUsers;

import java.util.List;

@Dao
public interface GitHubUsersDao {

    @Query("SELECT * FROM githubusers")
    List<GitHubUsers> listAll();

    @Insert
    void insertAll(List<GitHubUsers> items);

    @Insert
    void insertItem(GitHubUsers item);

    @Delete
    void delete(GitHubUsers item);

    @Update
    void update (GitHubUsers item);

    @Query("DELETE FROM githubusers")
    void deleteAll();
}
