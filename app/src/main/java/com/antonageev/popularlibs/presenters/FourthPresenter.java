package com.antonageev.popularlibs.presenters;

import android.util.Log;

import com.antonageev.popularlibs.FourthView;
import com.antonageev.popularlibs.IFourthPresenterCallBack;
import com.antonageev.popularlibs.models.FourthModel;
import com.antonageev.popularlibs.models.GitHubUsers;

import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class FourthPresenter extends BasePresenter<FourthModel, FourthView> implements IFourthPresenterCallBack {

    private final String TAG = FourthPresenter.class.getSimpleName();

    private Disposable resultTextDisposable;

    public FourthPresenter() {
        model = new FourthModel(this);
    }

    public List<GitHubUsers> getListUsersFromModel() {
        return model.getListUsers();
    }

    @Override
    protected void updateView() {
        onModelUpdated();
    }


    public void bindView(FourthView view, Consumer<String> textConsumer) {
        super.bindView(view);
        resultTextDisposable = model.getSubject().subscribe(textConsumer);
        model.getSubject().onNext(model.getResultText());
    }

    @Override
    public void unbindView() {
        super.unbindView();
        if (resultTextDisposable != null && !resultTextDisposable.isDisposed()) resultTextDisposable.dispose();

    }

    public Disposable presenterLoadUsers() {
        return model.modelRequestUsers();
    }

    public Disposable presenterLoadSingleUser(String user) {
        return model.modelRequestSingleUser(user);
    }

    public Disposable presenterUploadToRoom() {
        return model.uploadToRoomDB();
    }

    public Disposable presenterGetDataFromRoom() {
        return model.getFromRoomDB();
    }

    public Disposable presenterClearRoom() {
        return model.deleteFromRoomDB();
    }

    public Disposable presenterUploadToSugar() {
        return model.uploadToSugarDB();
    }

    public Disposable presenterGetDataFromSugar() {
        return model.getFromSugarDB();
    }

    public Disposable presenterClearSugar() {
        return model.deleteFromSugarDB();
    }

    @Override
    public void onModelUpdated() {
        Log.wtf(TAG, "onModelUpdated invoked, view(): " + view());
        if (view() != null && model != null){
            view().onUpdateViews(model.getListUsers());
        }
    }

    @Override
    public void onModelUpdateFailed(String message) {
        if (view() != null){
            view().onShowToast(message);
        }
    }

    @Override
    public void onSingleUserUpdated() {
        view().onShowMessageDialog(model.getTextDataAboutUser());
    }

    @Override
    public void onDBResultReturned() {
        view().onUpdateResultTextView(model.getResultText());
    }
}
