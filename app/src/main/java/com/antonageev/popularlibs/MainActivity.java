package com.antonageev.popularlibs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
        presenter = new Presenter(this);
    }

    @Override
    public void onClick(View v) {
        presenter.buttonClick(v.getId());
    }

    @Override
    public void setButtonText(int btnIndex, int value) {
        btnCounter = findViewById(btnIndex);
        btnCounter.setText("Количество = " + value);
    }
}
