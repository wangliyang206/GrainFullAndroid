package com.zqw.mobile.grainfull.mvp.model.entity;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: CateBean
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/9/4 16:36
 */
public class CateBean {
    public CateBean() {
    }

    public CateBean(String iconUrl, String categoryName, String categoryCode, List<CateBean> cateList) {
        this.iconUrl = iconUrl;
        this.categoryName = categoryName;
        this.categoryCode = categoryCode;
        this.cateList = cateList;
    }

    private String iconUrl;
    private String categoryName;
    private String categoryCode;
    private List<CateBean> cateList;

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public List<CateBean> getCateList() {
        return cateList;
    }

    public void setCateList(List<CateBean> cateList) {
        this.cateList = cateList;
    }
}
