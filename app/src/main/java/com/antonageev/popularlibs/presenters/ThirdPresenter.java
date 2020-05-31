package com.antonageev.popularlibs.presenters;

import android.graphics.Bitmap;
import android.util.Log;

import com.antonageev.popularlibs.ThirdView;
import com.antonageev.popularlibs.models.ThirdModel;

import java.io.File;
import java.io.FileOutputStream;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ThirdPresenter extends BasePresenter<ThirdModel, ThirdView> {

    private final String IMAGE_FOLDER = "/myImage";
    private final String TAG = ThirdPresenter.class.getSimpleName();

    @Override
    protected void updateView() {

    }

    public Disposable convertFromJpgToPng(Bitmap bm, File dest) {

        Single observable = Single.create(e -> {

            File myDir = new File(String.format("%s%s", dest.toString(), IMAGE_FOLDER));
            Log.d(TAG, "convertFromJpgToPng: myDir: " + myDir.toString());
            Boolean mkDirRes = false;
            if (!myDir.exists()) {
                mkDirRes = myDir.mkdirs();
            }
            Log.d(TAG, "convertFromJpgToPng: mkDirRes: " + mkDirRes);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException exc) {
                exc.printStackTrace();
            }

            FileOutputStream out = new FileOutputStream(String.format("%s/result.png", myDir.toString()));
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            e.onSuccess(true);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());


        Disposable disposable = observable.subscribe(
                (result) -> {
                    view().showResult("Convert successful");
                },
                (e) ->{
                    ((Throwable)e).printStackTrace();
                    view().showResult("Convert failed");
                });

        return disposable;
    }
}
