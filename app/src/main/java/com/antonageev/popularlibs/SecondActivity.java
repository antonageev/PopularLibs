package com.antonageev.popularlibs;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class SecondActivity extends AppCompatActivity {
    private EditText editText;
    private TextView textView;
    TextWatcher watcher;
    Button eventBusBtn;
    TextView eventTextView1;
    TextView eventTextView2;
    PublishSubject subject;

    Observable<String> observable1;
    Observable<String> observable2;

    Observer<String> observer1;
    Observer<String> observer2;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initViews();

        initObservingItems();
        
        initListeners();
        subscribeItems();
    }

    private void initViews() {
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        eventBusBtn = findViewById(R.id.eventBusBtn);
        eventBusBtn.setText("Event!!!");
        eventTextView1 = findViewById(R.id.eventTextView1);
        eventTextView2 = findViewById(R.id.eventTextView2);

    }

    private void initListeners() {
        editText.addTextChangedListener(watcher);
        eventBusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject.onNext("Event!!!");
            }
        });
    }

    private void subscribeItems() {
        subject.subscribe(observer1);
        subject.subscribe(observer2);
        observable1.subscribe(subject);
        observable2.subscribe(subject);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initObservingItems() {
        watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textView.setText(s);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        Function<Long, String> longToString = new Function<Long, String>() {
            @Override
            public String apply(Long aLong) throws Exception {
                return aLong.toString();
            }
        };

        observable1 = Observable.interval(1400, TimeUnit.MILLISECONDS).map(longToString).observeOn(AndroidSchedulers.mainThread());

        observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                try {
                    String[] array = new String[]{"A", "B", "C", "D", "E", "F", "G", "H"};
                    for (String s : array) {
                        e.onNext(s);
                        Thread.sleep(2000);
                    }
                } catch (InterruptedException exc) {
                    e.onError(exc);
                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());


        observer1 = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

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

        observer2 = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

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
