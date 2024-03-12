package com.zqw.mobile.grainfull.mvp.model.entity;

import com.zqw.mobile.grainfull.mvp.ui.widget.trendchart.OnTextRealization;
import com.zqw.mobile.grainfull.mvp.ui.widget.trendchart.SimpleMultipleColumnView;

/**
 * 趋势图
 */
public class SimpleMultipleColumnTxtBean implements SimpleMultipleColumnView.OnSimpleMultipleColumnViewXyAxisTextRealization<SimpleMultipleColumnTxtBean>, OnTextRealization<SimpleMultipleColumnTxtBean> {
    private String txt;

    private SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean level1;
    private SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean level2;
    private SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean level3;
    private SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean level4;
    private SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean level5;

    public SimpleMultipleColumnTxtBean(String txt, float number) {
        this.txt = txt;
        this.level1 = new SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean(number);
        this.level2 = new SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean(number);
        this.level3 = new SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean(number);
        this.level4 = new SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean(number);
        this.level5 = new SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean(number);
    }

    public SimpleMultipleColumnTxtBean(String txt, float level1, float level2, float level3, float level4, float level5) {
        this.txt = txt;
        this.level1 = new SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean(level1);
        this.level2 = new SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean(level2);
        this.level3 = new SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean(level3);
        this.level4 = new SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean(level4);
        this.level5 = new SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean(level5);
    }

    @Override
    public String getText() {
        return txt;
    }

    @Override
    public SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean getLevel1() {
        return level1;
    }

    @Override
    public SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean getLevel2() {
        return level2;
    }

    @Override
    public SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean getLevel3() {
        return level3;
    }

    @Override
    public SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean getLevel4() {
        return level4;
    }

    @Override
    public SimpleMultipleColumnView.SimpleMultipleColumnViewChildBean getLevel5() {
        return level5;
    }

    @Override
    public SimpleMultipleColumnTxtBean getBean() {
        return this;
    }
}
