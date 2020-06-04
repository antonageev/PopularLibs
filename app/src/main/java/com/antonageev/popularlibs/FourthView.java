package com.antonageev.popularlibs;

import com.antonageev.popularlibs.models.GitHubUsers;

import java.util.List;

public interface FourthView {
    void onUpdateViews(List<GitHubUsers> users); // when received new list of users from Retrofit
    void onShowMessageDialog(String data); // when received String data about one user.
    void onUpdateResultTextView(String message); //when operations with DBs are processed
}
