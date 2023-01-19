package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: SevenStatistics
 * @Description: 友盟统计 - 七日统计数据
 * @Author: WLY
 * @CreateDate: 2023/1/19 9:00
 */
public class SevenStatistics {
    public SevenStatistics() {
    }

    public SevenStatistics(String date, String value) {
        this.date = date;
        this.value = value;
    }

    // 统计日期
    private String date;
    // 统计数值(访问量)
    private String value;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
