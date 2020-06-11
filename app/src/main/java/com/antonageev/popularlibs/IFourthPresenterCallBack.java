package com.antonageev.popularlibs;

public interface IFourthPresenterCallBack {
    void onModelUpdated();
    void onModelUpdateFailed(String message);
    void onSingleUserUpdated();
    void onDBResultReturned();
}
