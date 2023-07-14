package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: HomeContentInfo
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/7/14 17:29
 */
public class HomeContentInfo {
    public HomeContentInfo() {
    }

    public HomeContentInfo(String image, String name) {
        this.image = image;
        this.name = name;
    }

    // 商品图片
    private String image;
    // 商品名称
    private String name;

    // 是否加载中
    private boolean loading;

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
