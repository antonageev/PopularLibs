package com.antonageev.popularlibs;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class SecondActivity extends AppCompatActivity {
    private EditText editText;
    private TextView textView;
    TextWatcher watcher;
    Button eventBusBtn;
    Button stopLeft;
    Button stopRight;
    Button startBus;
    TextView eventTextView1;
    TextView eventTextView2;
    PublishSubject subject;

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

        initObservingItems();
        
        initListeners();

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
        editText.addTextChangedListener(watcher);
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
