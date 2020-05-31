package com.antonageev.popularlibs.databases;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class GitUsers {

    @ColumnInfo(name = "login")
    private String login;

    @ColumnInfo(name = "avatar_url")
    private String avatarUrl;

    @ColumnInfo(name = "url")
    private String url;

    public GitUsers() {
    }

    public GitUsers(String login, String avatarUrl, String url) {
        this.login = login;
        this.avatarUrl = avatarUrl;
        this.url = url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
