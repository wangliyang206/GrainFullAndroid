package com.zqw.mobile.grainfull.mvp.model.entity;

import com.zqw.mobile.grainfull.mvp.ui.widget.trendchart.SimpleCandleView;

public class SimpleCandleViewTxtBean implements SimpleCandleView.OnSimpleCandleViewXyAxisTextRealization<SimpleCandleViewTxtBean> {
    private String txt;

    private int topLevelHigh;
    private int topLevelLow;

    private int bottomLevelHigh;
    private int bottomLevelLow;

    public SimpleCandleViewTxtBean(String txt, int number) {
        this.txt = txt;
        this.topLevelHigh = number;
        this.topLevelLow = number;
        this.bottomLevelHigh = number;
        this.bottomLevelLow = number;
    }

    public SimpleCandleViewTxtBean(String txt, int topLevelHigh, int topLevelLow, int bottomLevelHigh, int bottomLevelLow) {
        this.txt = txt;
        this.topLevelHigh = topLevelHigh;
        this.topLevelLow = topLevelLow;
        this.bottomLevelHigh = bottomLevelHigh;
        this.bottomLevelLow = bottomLevelLow;
    }

    @Override
    public String getText() {
        return txt;
    }

    @Override
    public int getTopLevelHigh() {
        return topLevelHigh;
    }

    @Override
    public int getTopLevelLow() {
        return topLevelLow;
    }

    @Override
    public int getBottomLevelHigh() {
        return bottomLevelHigh;
    }

    @Override
    public int getBottomLevelLow() {
        return bottomLevelLow;
    }


    @Override
    public SimpleCandleViewTxtBean getBean() {
        return this;
    }
}
