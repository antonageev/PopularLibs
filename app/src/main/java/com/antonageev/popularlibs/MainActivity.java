package com.antonageev.popularlibs;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {
    private Presenter presenter;
    private Button btnCounter;
    private Button btnCounter1;
    private Button btnCounter2;
    private Button btnCounter3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCounter1 = findViewById(R.id.btnCounter1);
        btnCounter2 = findViewById(R.id.btnCounter2);
        btnCounter3 = findViewById(R.id.btnCounter3);
        btnCounter1.setOnClickListener(this);
        btnCounter2.setOnClickListener(this);
        btnCounter3.setOnClickListener(this);

        if (savedInstanceState == null) {
            presenter = new Presenter();
        } else {
            presenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        presenter.buttonClick(v.getId());
    }

    @Override
    public void setButtonText(int btnIndex, int value) {
        btnCounter = findViewById(btnIndex);
        btnCounter.setText("Количество = " + value);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterManager.getInstance().savePresenter(presenter, outState);
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
        presenter.bindView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unbindView();
    }
}
