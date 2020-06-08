package com.antonageev.popularlibs.dagger;

import com.antonageev.popularlibs.models.GitHubUsers;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface ApiGson {
    @GET("/users")
    Single<List<GitHubUsers>> loadUsers();
}
