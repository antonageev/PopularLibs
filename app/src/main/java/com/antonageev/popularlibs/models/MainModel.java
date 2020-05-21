package com.antonageev.popularlibs.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;

public class MainModel {
    private Map<Integer, Integer> mMap;
    public MainModel(){
        mMap = new HashMap<>();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getElementValueAtIndex(int _index){
        return mMap.getOrDefault(_index, 0);
    }
    public void setElementValueAtIndex(int _index, int value){
        mMap.put(_index, value);
    }

    public Map<Integer, Integer> getmMap() {
        return mMap;
    }
}
