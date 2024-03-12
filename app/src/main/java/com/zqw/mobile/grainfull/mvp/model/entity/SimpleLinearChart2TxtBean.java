package com.zqw.mobile.grainfull.mvp.model.entity;

import com.zqw.mobile.grainfull.mvp.ui.widget.trendchart.SimpleLinearChart2;

/**
 * 趋势图实体类
 */
public class SimpleLinearChart2TxtBean implements SimpleLinearChart2.OnSimpleLinearChart2XyAxisTextRealization<SimpleLinearChart2TxtBean> {
    private String txt;
    private int number;
    private String remark;

    public SimpleLinearChart2TxtBean(String txt, int number) {
        this.txt = txt;
        this.number = number;
    }

    public SimpleLinearChart2TxtBean(String txt, int number, String remark) {
        this.txt = txt;
        this.number = number;
        this.remark = remark;
    }


    @Override
    public String getText() {
        return txt;
    }


    @Override
    public SimpleLinearChart2TxtBean getBean() {
        return this;
    }

    @Override
    public String getRemark() {
        return null;
    }

    @Override
    public int getNumber() {
        return number;
    }
}
