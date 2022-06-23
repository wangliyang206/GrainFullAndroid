package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * 包名： com.zqw.mobile.recycling.model
 * 对象名： UploadPicResponse
 * 描述：上传照片响应结构
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/4/10 16:10
 */

public class UploadPicResponse {
    public UploadPicResponse() {
    }

    public UploadPicResponse(String urlScale, String url) {
        this.urlScale = urlScale;
        this.url = url;
    }

    private String urlScale = "";
    private String url = "";

    public String getUrlScale() {
        return urlScale;
    }

    public void setUrlScale(String urlScale) {
        this.urlScale = urlScale;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
