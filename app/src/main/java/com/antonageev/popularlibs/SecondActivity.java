package com.antonageev.popularlibs;

import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.antonageev.popularlibs.presenters.SecondPresenter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class SecondActivity extends AppCompatActivity implements SecondView {
    private EditText editText;
    private TextView textView;
    Button eventBusBtn;
    Button stopLeft;
    Button stopRight;
    Button startBus;
    TextView eventTextView1;
    TextView eventTextView2;
    PublishSubject subject;
    SecondPresenter secondPresenter;

    Disposable disposable;

    Observable<String> observable1;
    Observable<String> observable2;

    DisposableObserver<String> observer1;
    DisposableObserver<String> observer2;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initViews();

        if (savedInstanceState == null) {
            secondPresenter = new SecondPresenter();
        } else {
            secondPresenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        initObservingItems();
        
        initListeners();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterManager.getInstance().savePresenter(secondPresenter, outState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Observable<String> textObservable = Observable.create(e -> editText.addTextChangedListener((SimpleTextWatcher) s -> e.onNext(s.toString())));

        disposable = secondPresenter.bindView(textObservable, (s -> textView.setText(s)));
    }

    @Override
    protected void onPause() {
        super.onPause();
        secondPresenter.unbindView(disposable);
    }

    private void initViews() {
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        eventBusBtn = findViewById(R.id.eventBusBtn);
        eventBusBtn.setText("Event!!!");
        eventTextView1 = findViewById(R.id.eventTextView1);
        eventTextView2 = findViewById(R.id.eventTextView2);

        stopLeft = findViewById(R.id.stopLeft);
        stopLeft.setText("Stop Left");

        stopRight = findViewById(R.id.stopRight);
        stopRight.setText("Stop Right");

        startBus = findViewById(R.id.startBus);
        startBus.setText("Start Bus");
    }

    private void initListeners() {

        eventBusBtn.setOnClickListener(v -> subject.onNext("Event!!!"));
        stopLeft.setOnClickListener(v -> observer1.dispose());
        stopRight.setOnClickListener(v -> observer2.dispose());
        startBus.setOnClickListener(v -> {subscribeItems(); startBus.setEnabled(false);});
    }

    private void subscribeItems() {
        subject.subscribe(observer1);
        subject.subscribe(observer2);
        observable1.subscribe(subject);
        observable2.subscribe(subject);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initObservingItems() {

        Function<Long, String> longToString = aLong -> aLong.toString();

        observable1 = Observable.interval(1400, TimeUnit.MILLISECONDS).map(longToString).observeOn(AndroidSchedulers.mainThread());

        observable2 = Observable.create((ObservableOnSubscribe<String>) e -> {
            String[] arr = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
            for (String s : arr) {
                e.onNext(s);
                Thread.sleep(2000);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());

        observer1 = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                eventTextView1.setText(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        observer2 = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                eventTextView2.setText(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        subject = PublishSubject.create();
    }
}
