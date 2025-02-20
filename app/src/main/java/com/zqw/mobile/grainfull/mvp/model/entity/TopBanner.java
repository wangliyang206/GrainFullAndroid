package com.zqw.mobile.grainfull.mvp.model.entity;

import com.stx.xhb.androidx.entity.BaseBannerInfo;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: TopBanner
 * @Description: 顶部横幅
 * @Author: WLY
 * @CreateDate: 2025/2/18 15:13
 */
public class TopBanner implements BaseBannerInfo {
    public TopBanner() {
    }

    public TopBanner(String url, String title) {
        this.url = url;
        this.title = title;
    }

    // 图片地址
    private String url;
    // 标题
    private String title;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Object getXBannerUrl() {
        return url;
    }

    @Override
    public String getXBannerTitle() {
        return title;
    }
}
