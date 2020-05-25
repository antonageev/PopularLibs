package com.antonageev.popularlibs;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.antonageev.popularlibs.presenters.ThirdPresenter;

public class ThirdActivity extends AppCompatActivity implements ThirdView {

    Button convertButton;
    ImageView jpgImage;
    ImageView pngImage;
    ThirdPresenter thirdPresenter;
    private static String TAG = ThirdActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        if (savedInstanceState == null) {
            thirdPresenter = new ThirdPresenter();
        } else {
            thirdPresenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }

        initViews();
        initListeners();
    }

    private void initListeners() {
        convertButton.setOnClickListener(v -> thirdPresenter.convertFromJpgToPng());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterManager.getInstance().savePresenter(thirdPresenter, outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String folder = this.getFilesDir().getPath();
        thirdPresenter.bindView(this, folder, folder, "picture");
    }

    @Override
    protected void onPause() {
        super.onPause();
        thirdPresenter.unbindView();
    }

    private void initViews() {
        convertButton = findViewById(R.id.convertButton);
        convertButton.setText("Convert");
        jpgImage = findViewById(R.id.jpgImageView);
        pngImage = findViewById(R.id.pngImageView);
    }

    @Override
    public void setImages(Bitmap bm1, Bitmap bm2) {
        if (bm1 != null) jpgImage.setImageBitmap(bm1);
        if (bm2 != null) pngImage.setImageBitmap(bm2);
    }
}
