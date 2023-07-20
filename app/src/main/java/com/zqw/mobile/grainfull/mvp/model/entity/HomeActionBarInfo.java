package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: HomeContentInfo
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/7/14 17:29
 */
public class HomeActionBarInfo {
    public HomeActionBarInfo() {
    }

    public HomeActionBarInfo(int image, String name) {
        this.image = image;
        this.name = name;
    }

    // 商品图片
    private int image;
    // 商品名称
    private String name;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
