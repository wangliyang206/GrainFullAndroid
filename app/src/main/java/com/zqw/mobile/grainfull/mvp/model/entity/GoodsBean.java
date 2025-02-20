package com.zqw.mobile.grainfull.mvp.model.entity;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: GoodsBean
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/8/30 12:11
 */
public class GoodsBean implements MultiItemEntity {
    public GoodsBean() {
    }

    public GoodsBean(String imgUrl, String description, String price, String tag, String des1, String des2, int type) {
        this.imgUrl = imgUrl;
        this.description = description;
        this.price = price;
        this.tag = tag;
        this.des1 = des1;
        this.des2 = des2;
        this.type = type;
    }

    private String imgUrl;
    private String description;
    private String price;
    private String tag;
    private String des1;
    private String des2;
    private int type;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDes1() {
        return des1;
    }

    public void setDes1(String des1) {
        this.des1 = des1;
    }

    public String getDes2() {
        return des2;
    }

    public void setDes2(String des2) {
        this.des2 = des2;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
