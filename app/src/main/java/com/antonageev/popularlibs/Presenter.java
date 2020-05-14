package com.antonageev.popularlibs;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class Presenter extends BasePresenter<Model, MainView>{

    public Presenter(){
        model = new Model();
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
            model = new Model();
        }
        updateView();
    }
}
