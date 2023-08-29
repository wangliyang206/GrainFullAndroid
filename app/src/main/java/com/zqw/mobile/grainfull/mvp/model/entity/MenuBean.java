package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: MenuBean
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/8/18 9:22
 */
public class MenuBean {
    public MenuBean() {
    }

    public MenuBean(String menuIcon, String menuName, String menuCode, String h5url) {
        this.menuIcon = menuIcon;
        this.menuName = menuName;
        this.menuCode = menuCode;
        this.h5url = h5url;
    }

    private String menuIcon;
    private String menuName;
    private String menuCode;
    private String h5url;

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public String getH5url() {
        return h5url;
    }

    public void setH5url(String h5url) {
        this.h5url = h5url;
    }
}
