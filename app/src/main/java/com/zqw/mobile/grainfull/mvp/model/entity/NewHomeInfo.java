package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: NewHomeInfo
 * @Description: 首页2.0实体类
 * @Author: WLY
 * @CreateDate: 2023/7/14 15:47
 */
public class NewHomeInfo {
    public NewHomeInfo() {
    }

    public NewHomeInfo(HomeTab tab) {
        this.tab = tab;
    }
    // 第一梯队

    // 第二梯队
    // 第三梯队
    private HomeTab tab;

    public HomeTab getTab() {
        return tab;
    }

    public void setTab(HomeTab tab) {
        this.tab = tab;
    }
}
