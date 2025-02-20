package com.zqw.mobile.grainfull.mvp.model.entity;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: GoodsInfo
 * @Description:
 * @Author: WLY
 * @CreateDate: 2025/2/18 17:02
 */
public class GoodsInfo {
    private String originalPrice;
    private String specialPrice;
    private List<String> tagList;
    private String goodsName;
    private List<AppraiseBean> appraiseList;

    public GoodsInfo(String originalPrice, String specialPrice, List<String> tagList, String goodsName, List<AppraiseBean> appraiseList) {
        this.originalPrice = originalPrice;
        this.specialPrice = specialPrice;
        this.tagList = tagList;
        this.goodsName = goodsName;
        this.appraiseList = appraiseList;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(String specialPrice) {
        this.specialPrice = specialPrice;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public List<AppraiseBean> getAppraiseList() {
        return appraiseList;
    }

    public void setAppraiseList(List<AppraiseBean> appraiseList) {
        this.appraiseList = appraiseList;
    }
}
