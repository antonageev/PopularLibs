package com.antonageev.popularlibs.databases;

import com.orm.SugarRecord;

public class SugarModel extends SugarRecord {
    private String login;
    private String avatarUrl;
    private String url;

    public SugarModel() {
    }

    public SugarModel(String login, String avatarUrl, String url) {
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
