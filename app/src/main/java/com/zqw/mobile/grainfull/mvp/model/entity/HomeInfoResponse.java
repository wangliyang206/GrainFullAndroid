package com.zqw.mobile.grainfull.mvp.model.entity;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: HomeInfoResponse
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/8/18 9:18
 */
public class HomeInfoResponse {
    public HomeInfoResponse() {
    }

    public HomeInfoResponse(List<BannerBean> bannerList, List<TabBean> tabList, String adUrl, List<MenuBean> nineMenuList) {
        this.bannerList = bannerList;
        this.tabList = tabList;
        this.adUrl = adUrl;
        this.nineMenuList = nineMenuList;
    }

    // 轮播图
    private List<BannerBean> bannerList;
    // Tab数据
    private List<TabBean> tabList;
    // 广告地址
    private String adUrl;
    // 菜单
    private List<MenuBean> nineMenuList;

    public List<BannerBean> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<BannerBean> bannerList) {
        this.bannerList = bannerList;
    }

    public List<TabBean> getTabList() {
        return tabList;
    }

    public void setTabList(List<TabBean> tabList) {
        this.tabList = tabList;
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public List<MenuBean> getNineMenuList() {
        return nineMenuList;
    }

    public void setNineMenuList(List<MenuBean> nineMenuList) {
        this.nineMenuList = nineMenuList;
    }
}
