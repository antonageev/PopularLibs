package com.antonageev.popularlibs;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.antonageev.popularlibs.presenters.MainPresenter;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {
    private MainPresenter mainPresenter;
    private Button btnCounter;
    private Button btnCounter1;
    private Button btnCounter2;
    private Button btnCounter3;
    private Button btnMove;
    private Button btnMoveTo3;
    private final String TAG = MainActivity.class.getSimpleName();

    Observable<Integer> observable;
    Observer<Integer> observer;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        setListeners();

        if (savedInstanceState == null) {
            mainPresenter = new MainPresenter();
        } else {
            mainPresenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                mainPresenter.buttonClick(integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

    }

    private void setListeners() {
        btnCounter1.setOnClickListener(this);
        btnCounter2.setOnClickListener(this);
        btnCounter3.setOnClickListener(this);
        btnMove.setOnClickListener(this);
        btnMoveTo3.setOnClickListener(this);
    }

    private void initViews() {
        btnCounter1 = findViewById(R.id.btnCounter1);
        btnCounter2 = findViewById(R.id.btnCounter2);
        btnCounter3 = findViewById(R.id.btnCounter3);
        btnMove = findViewById(R.id.moveToAnotherActivity);
        btnMoveTo3 = findViewById(R.id.btnMoveToThirdActivity);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.moveToAnotherActivity) {
            Intent intent = new Intent(this, SecondActivity.class);
            startActivity(intent);
            return;
        }

        if (v.getId() == R.id.btnMoveToThirdActivity) {
            Intent intent = new Intent(this, ThirdActivity.class);
            startActivity(intent);
            return;
        }

        observable = Observable.just(v.getId());
        observable.subscribe(observer);
    }

    @Override
    public void setButtonText(int btnIndex, int value) {
        btnCounter = findViewById(btnIndex);
        btnCounter.setText("Количество = " + value);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterManager.getInstance().savePresenter(mainPresenter, outState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void showCounters(Map<Integer, Integer> map) {
        setButtonText(R.id.btnCounter1, map.getOrDefault(R.id.btnCounter1, 0));
        setButtonText(R.id.btnCounter2, map.getOrDefault(R.id.btnCounter2, 0));
        setButtonText(R.id.btnCounter3, map.getOrDefault(R.id.btnCounter3, 0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainPresenter.bindView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainPresenter.unbindView();
    }
}
