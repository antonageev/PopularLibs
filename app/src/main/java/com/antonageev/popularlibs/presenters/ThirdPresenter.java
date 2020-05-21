package com.antonageev.popularlibs.presenters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.antonageev.popularlibs.ThirdView;
import com.antonageev.popularlibs.models.ThirdModel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ThirdPresenter extends BasePresenter<ThirdModel, ThirdView> {

    public ThirdPresenter() {
        model = new ThirdModel();
    }

    @Override
    protected void updateView() {
        FileInputStream inputStream1 = null;
        FileInputStream inputStream2 = null;
        try {
            inputStream1 = new FileInputStream(model.getSource() + "/" + model.getPictureName() + ".jpg");
            inputStream2 = new FileInputStream(model.getDestination() + "/" + model.getPictureName() + ".png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        view().setImages(BitmapFactory.decodeStream(inputStream1), BitmapFactory.decodeStream(inputStream2));

        try {
            if (inputStream1 != null) inputStream1.close();
            if (inputStream2 != null) inputStream2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void convertFromJpgToPng() {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(model.getSource() + "/" + model.getPictureName() + ".jpg");
            outputStream = new FileOutputStream(model.getDestination() + "/" + model.getPictureName() + ".png");

            Single<FileInputStream> single = Single.just(inputStream);

            final FileOutputStream innerOut = outputStream;

            DisposableSingleObserver<FileInputStream> dso = new DisposableSingleObserver<FileInputStream>() {
                @Override
                public void onSuccess(FileInputStream fileInputStream) {
                    BitmapFactory.decodeStream(fileInputStream).compress(Bitmap.CompressFormat.PNG, 75, innerOut);
                }
                @Override
                public void onError(Throwable e) {
                }
            };

            single.subscribeOn(Schedulers.io()).subscribe(dso);
            dso.dispose();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        updateView();
    }

    public void bindView(ThirdView view, String srcFolder, String dstFolder, String pictureName) {
        model.setSource(srcFolder);
        model.setDestination(dstFolder);
        model.setPictureName(pictureName);
        super.bindView(view);
    }

//    public void convertFromJpgToPngBytes(String src, String dst, String pictureName) {
//        try {
//            FileInputStream inputStream = new FileInputStream(src + "/" + pictureName + ".jpg");
//            FileOutputStream outputStream = new FileOutputStream(dst + "/" + pictureName + ".png");
//
//
//            Observable<Integer> single = Observable.create(e -> {
//                int b;
//                while ((b = inputStream.read()) != -1){
//                    e.onNext(b);
//                }
//            });
//
//            DisposableObserver<Integer> disposableObserver = new DisposableObserver<Integer>() {
//                @Override
//                public void onNext(Integer integer) {
//                    try {
//                        outputStream.write(integer);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void onError(Throwable e) {
//
//                }
//
//                @Override
//                public void onComplete() {
//
//                }
//            };
//
////            DisposableSingleObserver<FileInputStream> dso = new DisposableSingleObserver<FileInputStream>() {
////                @Override
////                public void onSuccess(FileInputStream fileInputStream) {
////                    BitmapFactory.decodeStream(fileInputStream).compress(Bitmap.CompressFormat.PNG, 75, outputStream);
////                }
////
////                @Override
////                public void onError(Throwable e) {
////
////                }
////            };
//
////            single.subscribeOn(Schedulers.io()).subscribe(dso);
////            dso.dispose();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//    }
}
