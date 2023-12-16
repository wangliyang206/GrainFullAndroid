package com.zqw.mobile.grainfull.mvp.model.entity;

public class ImageUpload {
    // 原图
    private String url = "";
    // 缩略图
    private String scaleUrl = "";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getScaleUrl() {
        return scaleUrl;
    }

    public void setScaleUrl(String scaleUrl) {
        this.scaleUrl = scaleUrl;
    }
}
