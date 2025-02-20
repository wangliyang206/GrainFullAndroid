package com.zqw.mobile.grainfull.mvp.model.entity;

import com.chad.library.adapter.base.entity.SectionEntity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: AppraiseBean
 * @Description:
 * @Author: WLY
 * @CreateDate: 2025/2/18 15:57
 */
public class AppraiseBean implements SectionEntity {
    private String headerUrl;
    private String userName;
    private String content;
    private int type;
    private String color;
    private String size;
    private String url;

    public AppraiseBean(String headerUrl, String userName, String content, int type, String color, String size) {
        this.headerUrl = headerUrl;
        this.userName = userName;
        this.content = content;
        this.type = type;
        this.color = color;
        this.size = size;
    }

    public AppraiseBean(String url, int type) {
        this.url = url;
        this.type = type;
    }

    // Getters and Setters
    public String getHeaderUrl() {
        return headerUrl;
    }

    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean isHeader() {
        return this.type == 1;
    }

    @Override
    public int getItemType() {
        return DefaultImpls.getItemType(this);
    }
}
