package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: CategoryBean
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/9/5 9:19
 */
public class CategoryBean {
    public CategoryBean() {
    }

    public CategoryBean(String name, String code, boolean isSelect, String fillet) {
        this.name = name;
        this.code = code;
        this.isSelect = isSelect;
        this.fillet = fillet;
    }

    private String name;
    private String code;
    private boolean isSelect;
    private String fillet;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getFillet() {
        return fillet;
    }

    public void setFillet(String fillet) {
        this.fillet = fillet;
    }
}
