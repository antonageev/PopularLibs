package com.antonageev.popularlibs;

public class Presenter {
    private Model model;
    private MainView view;

    public Presenter(MainView view) {
        model = new Model();
        this.view = view;
    }
    private int calcNewModelValue(int modelElementIndex){
        int currentValue = model.getElementValueAtIndex(modelElementIndex);
        return currentValue + 1;
    }
    public void buttonClick(int btnIndex){
        int newModelValue;
        switch (btnIndex){
            case R.id.btnCounter1:
                newModelValue = calcNewModelValue(0);
                model.setElementValueAtIndex(0, newModelValue);
                view.setButtonText(btnIndex, newModelValue);
                break;
            case R.id.btnCounter2:
                newModelValue = calcNewModelValue(1);
                model.setElementValueAtIndex(1, newModelValue);
                view.setButtonText(btnIndex, newModelValue);
                break;
            case R.id.btnCounter3:
                newModelValue = calcNewModelValue(2);
                model.setElementValueAtIndex(2, newModelValue);
                view.setButtonText(btnIndex, newModelValue);
                break;
        }
    }



}
