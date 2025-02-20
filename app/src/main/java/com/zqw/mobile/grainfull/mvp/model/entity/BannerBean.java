package com.zqw.mobile.grainfull.mvp.model.entity;

import java.util.List;

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

    public BannerBean(String colorId, String colorName, String thumb, List<String> imgList) {
        this.colorId = colorId;
        this.colorName = colorName;
        this.thumb = thumb;
        this.imgList = imgList;
    }

    public BannerBean(String colorId, String colorName, String thumb, List<String> imgList, boolean select) {
        this.colorId = colorId;
        this.colorName = colorName;
        this.thumb = thumb;
        this.imgList = imgList;
        this.select = select;
    }

    // 图片地址
    private String imgUrl;
    private String type;

    // 颜色Id
    private String colorId;
    // 颜色名称
    private String colorName;
    private String thumb;
    private List<String> imgList;
    private boolean select;

    public String getColorId() {
        return colorId;
    }

    public void setColorId(String colorId) {
        this.colorId = colorId;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

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
