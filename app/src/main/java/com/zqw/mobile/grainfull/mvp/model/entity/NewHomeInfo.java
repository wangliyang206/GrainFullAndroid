package com.zqw.mobile.grainfull.mvp.model.entity;

import java.util.List;

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

    public NewHomeInfo(List<HomeContentInfo> topList) {
        this.topList = topList;
    }

    public NewHomeInfo(HomeTab tab) {
        this.tab = tab;
    }
    // 第一梯队
    private List<HomeContentInfo> topList;
    // 第二梯队
    private List<HomeActionBarInfo> actionBarList;
    // 第三梯队
    private HomeTab tab;

    public List<HomeContentInfo> getTopList() {
        return topList;
    }

    public void setTopList(List<HomeContentInfo> topList) {
        this.topList = topList;
    }

    public List<HomeActionBarInfo> getActionBarList() {
        return actionBarList;
    }

    public void setActionBarList(List<HomeActionBarInfo> actionBarList) {
        this.actionBarList = actionBarList;
    }

    public HomeTab getTab() {
        return tab;
    }

    public void setTab(HomeTab tab) {
        this.tab = tab;
    }
}
