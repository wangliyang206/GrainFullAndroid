package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: GoodsEntity
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/8/14 17:18
 */
public class GoodsEntity {
    public GoodsEntity() {
    }

    public GoodsEntity(String labelPrice, String imgURL, String oldlabelPrice) {
        this.labelPrice = labelPrice;
        this.imgURL = imgURL;
        this.oldlabelPrice = oldlabelPrice;
    }

    // 标签价格
    public String labelPrice;
    // 图片地址
    public String imgURL;
    // 旧价格
    public String oldlabelPrice;

    public String getLabelPrice() {
        return labelPrice;
    }

    public void setLabelPrice(String labelPrice) {
        this.labelPrice = labelPrice;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getOldlabelPrice() {
        return oldlabelPrice;
    }

    public void setOldlabelPrice(String oldlabelPrice) {
        this.oldlabelPrice = oldlabelPrice;
    }
}
