package com.zqw.mobile.grainfull.mvp.model.entity;

import java.util.List;

public class ImageUploadResponse {
    // 通用结构
    private List<ImageUpload> imgList;

    public List<ImageUpload> getImgList() {
        return imgList;
    }

    public void setImgList(List<ImageUpload> imgList) {
        this.imgList = imgList;
    }
}
