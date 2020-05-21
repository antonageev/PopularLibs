package com.antonageev.popularlibs.presenters;

public abstract class BasePresenter<M, V> {
    protected M model;
    private V view;

    public void bindView(V view){
        this.view = view;
        if (setupDone()){
            updateView();
        }
    }

    public void setModel(M model) {
        this.model = model;
        if (setupDone()){
            updateView();
        }
    }

    public void unbindView() {
        this.view = null;
    }

    protected abstract void updateView();

    protected boolean setupDone(){
        return view() != null && model != null;
    }

    protected V view(){
        if (view == null) {
            return null;
        } else {
            return view;
        }
    }

}
