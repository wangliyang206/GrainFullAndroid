package com.zqw.mobile.grainfull.mvp.model.entity;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: ContentCateResponse
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/9/4 18:17
 */
public class ContentCateResponse {
    public ContentCateResponse() {
    }

    public ContentCateResponse(String bannerUrl, List<CateBean> cateList) {
        this.bannerUrl = bannerUrl;
        this.cateList = cateList;
    }

    private String bannerUrl;
    private List<CateBean> cateList;

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public List<CateBean> getCateList() {
        return cateList;
    }

    public void setCateList(List<CateBean> cateList) {
        this.cateList = cateList;
    }
}
