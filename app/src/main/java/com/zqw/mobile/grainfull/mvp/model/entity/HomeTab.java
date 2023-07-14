package com.zqw.mobile.grainfull.mvp.model.entity;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class HomeTab {
    ArrayList<String> tabTitleList = new ArrayList<>();

    public ArrayList<String> getTabTitleList() {
        return tabTitleList;
    }

    public void setTabTitleList(ArrayList<String> tabTitleList) {
        this.tabTitleList = tabTitleList;
    }

    @NonNull
    @Override
    public String toString() {
        return tabTitleList.toString();
    }
}
