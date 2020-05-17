package com.antonageev.popularlibs;

import java.util.Map;

public interface MainView {
    void setButtonText(int btnIndex, int value);
    void showCounters(Map<Integer, Integer> map);
}
