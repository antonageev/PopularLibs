package com.antonageev.popularlibs.presenters;

import com.antonageev.popularlibs.models.MainModel;
import com.antonageev.popularlibs.SecondView;
import com.antonageev.popularlibs.models.SecondModel;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class SecondPresenter extends BasePresenter<SecondModel, SecondView> {

    public SecondPresenter(){
        model = new SecondModel();
    }

    @Override
    protected void updateView() {

    }

    public Disposable bindView(Observable<String> observable, Consumer<String> consumer) {

        PublishSubject<String> publishSubject = PublishSubject.create();
        Disposable disposable = publishSubject.subscribe(consumer);
        publishSubject.onNext(model.getStoredText());
        disposable.dispose();


        return observable.map(s -> model.setStoredText(s)).subscribe(consumer);

    }


    public void unbindView(Disposable disposable) {
        super.unbindView();
        if (!disposable.isDisposed()) disposable.dispose();
    }
}
