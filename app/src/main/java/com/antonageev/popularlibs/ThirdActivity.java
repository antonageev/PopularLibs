package com.antonageev.popularlibs;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.DialogCompat;

import com.antonageev.popularlibs.presenters.ThirdPresenter;

import java.io.IOException;

import io.reactivex.disposables.Disposable;

public class ThirdActivity extends AppCompatActivity implements ThirdView {

    private final int REQUEST_PERMISSIONS_CODE = 99;
    private final int REQUEST_CODE = 100;

    private AlertDialog alert;
    private Disposable disposable;
    Button pickImageButton;
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

        checkPermissions();
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestPermits();
        } else {
            pickImageButton.setEnabled(true);
        }
    }

    private void requestPermits() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSIONS_CODE );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            pickImageButton.setEnabled(true);
        }
    }

    private void initListeners() {
        pickImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT).addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select file"), 100);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE || resultCode != RESULT_OK || data == null) return;
            Uri selectedFile = data.getData();
        try {
            Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedFile);
            disposable = thirdPresenter.convertFromJpgToPng(bm, Environment.getExternalStorageDirectory());
            alert.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterManager.getInstance().savePresenter(thirdPresenter, outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initViews() {
        jpgImage = findViewById(R.id.jpgImageView);
        pngImage = findViewById(R.id.pngImageView);
        pickImageButton = findViewById(R.id.pickImageBtn);
        pickImageButton.setText("Pick and convert");
        pickImageButton.setEnabled(false);

        alert = new AlertDialog.Builder(this)
                .setTitle("Converting")
                .setMessage("in process...")
                .setNegativeButton("Cancel", ((dialog, which) -> {if (!disposable.isDisposed()) disposable.dispose();}))
                .create();
    }

    @Override
    public void showResult(String forToast) {
        if (alert.isShowing()) alert.cancel();
        Toast.makeText(this, forToast, Toast.LENGTH_SHORT).show();
    }
}
