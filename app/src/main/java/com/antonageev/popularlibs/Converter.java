package com.antonageev.popularlibs;

import com.antonageev.popularlibs.databases.GitUsers;
import com.antonageev.popularlibs.databases.SugarModel;
import com.antonageev.popularlibs.models.GitHubUsers;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    public static List<GitHubUsers> convertToGitHubUsersFromSugar(List<SugarModel> sugarModelList) {
        List<GitHubUsers> resultList = new ArrayList<>();
        for (SugarModel item : sugarModelList) {
            GitHubUsers gitHubUser = new GitHubUsers();
            gitHubUser.setAvatarUrl(item.getAvatarUrl());
            gitHubUser.setUrl(item.getUrl());
            gitHubUser.setLogin(item.getLogin());
            resultList.add(gitHubUser);
        }
        return resultList;
    }

}
