package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: SevenStatistics
 * @Description: 统计时长
 * @Author: WLY
 * @CreateDate: 2023/1/19 9:00
 */
public class UmEvent {
    public UmEvent() {
    }

    public UmEvent(String id, String name, int count, String displayName) {
        this.id = id;
        this.name = name;
        this.count = count;
        this.displayName = displayName;
    }

    // ID
    private String id;
    // 事件名称
    private String name;
    // 统计次数
    private int count;
    // 显示名称
    private String displayName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
