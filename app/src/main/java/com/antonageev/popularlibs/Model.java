package com.antonageev.popularlibs;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.HashMap;
import java.util.Map;

public class Model{
    private Map<Integer, Integer> mMap;
    public Model(){
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
