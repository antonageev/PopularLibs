package com.antonageev.popularlibs.presenters;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.antonageev.popularlibs.MainView;
import com.antonageev.popularlibs.models.MainModel;

public class MainPresenter extends BasePresenter<MainModel, MainView> {

    public MainPresenter(){
        model = new MainModel();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private int calcNewModelValue(int modelElementIndex){
        int currentValue = model.getElementValueAtIndex(modelElementIndex);
        return currentValue + 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void buttonClick(int btnIndex) {
        int newModelValue;
        newModelValue = calcNewModelValue(btnIndex);
        model.setElementValueAtIndex(btnIndex, newModelValue);
        view().setButtonText(btnIndex, newModelValue);
    }


    @Override
    protected void updateView() {
        view().showCounters(model.getmMap());
    }

    @Override
    public void bindView(@NonNull MainView view) {
        super.bindView(view);
        if (model == null) {
            setModel(new MainModel());
        }
    }
}
