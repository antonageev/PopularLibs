package com.antonageev.popularlibs;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class PresenterManager {
    private static final String KEY_PRESENTER_ID = "presenterId";
    private static PresenterManager instance;
    private Map<Long, BasePresenter> presenters;
    private AtomicLong currentId;

    PresenterManager(){
        currentId = new AtomicLong();
        presenters = new HashMap<>();
    }

    public static PresenterManager getInstance() {
        if (instance == null){
            instance = new PresenterManager();
        }
        return instance;
    }

    public void savePresenter(BasePresenter<?, ?> presenter, Bundle outstate){
        long pId = currentId.incrementAndGet();
        presenters.put(pId, presenter);
        outstate.putLong(KEY_PRESENTER_ID, pId);
    }

    public <P extends BasePresenter<?, ?>> P restorePresenter(Bundle savedInstanceState) {
        long presenterId = savedInstanceState.getLong(KEY_PRESENTER_ID, -1);

        if (!presenters.containsKey(presenterId)) return (P) new Presenter();

        P presenter = (P) presenters.get(presenterId);
        presenters.remove(presenterId);
        return presenter;
    }
}
