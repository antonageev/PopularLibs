package com.antonageev.popularlibs.presenters;

import android.util.Log;

import com.antonageev.popularlibs.FourthView;
import com.antonageev.popularlibs.IFourthPresenterCallBack;
import com.antonageev.popularlibs.models.FourthModel;

import io.reactivex.disposables.Disposable;

public class FourthPresenter extends BasePresenter<FourthModel, FourthView> implements IFourthPresenterCallBack {

    private final String TAG = FourthPresenter.class.getSimpleName();

    public FourthPresenter() {
        model = new FourthModel(this);
    }

    @Override
    protected void updateView() {

    }

    public Disposable presenterLoadUsers() {
        return model.modelRequestUsers();
    }

    public Disposable presenterLoadSingleUser(String user) {
        return model.modelRequestSingleUser(user);
    }

    @Override
    public void onModelUpdated() {
        Log.wtf(TAG, "onModelUpdated invoked, view(): " + view());
        if (view() != null){
            view().onUpdateViews(model.getListUsers());
        }
    }



    @Override
    public void onModelUpdateFailed() {
        //TO DO: show message in Activity
        if (view() != null){
            view().onUpdateViews(null);
        }
    }

    @Override
    public void onSingleUserUpdated() {
        view().onShowMessageDialog(model.getTextDataAboutUser());
    }
}
