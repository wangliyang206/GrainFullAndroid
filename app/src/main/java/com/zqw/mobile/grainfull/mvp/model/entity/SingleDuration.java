package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: SevenStatistics
 * @Description: 统计时长
 * @Author: WLY
 * @CreateDate: 2023/1/19 9:00
 */
public class SingleDuration {
    public SingleDuration() {
    }

    public SingleDuration(String name, int value, double percent) {
        this.name = name;
        this.value = value;
        this.percent = percent;
    }

    // 时间区间单位秒
    private String name;
    // 启动次数/用户数
    private int value;
    // 此区间的时长占
    private double percent;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
}
