package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: CategoryModal
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/9/4 15:08
 */
public class CategoryModal {
    public CategoryModal() {
    }

    public CategoryModal(String iconUrl, String categoryName, String categoryCode, boolean isTitle) {
        this.iconUrl = iconUrl;
        this.categoryName = categoryName;
        this.categoryCode = categoryCode;
        this.isTitle = isTitle;
    }

    private String iconUrl;
    private String categoryName;
    private String categoryCode;
    private boolean isTitle;

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }
}
