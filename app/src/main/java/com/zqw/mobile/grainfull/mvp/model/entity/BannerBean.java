package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: BannerBean
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/8/18 9:21
 */
public class BannerBean {
    public BannerBean() {
    }

    public BannerBean(String imgUrl, String type) {
        this.imgUrl = imgUrl;
        this.type = type;
    }

    private String imgUrl;
    private String type;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
