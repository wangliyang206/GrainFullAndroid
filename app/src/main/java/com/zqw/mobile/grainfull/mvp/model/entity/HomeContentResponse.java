package com.zqw.mobile.grainfull.mvp.model.entity;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: HomeContentResponse
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/7/14 17:51
 */
public class HomeContentResponse {
    private List<HomeContentInfo> list;
    private int pages;

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<HomeContentInfo> getList() {
        return list;
    }

    public void setList(List<HomeContentInfo> list) {
        this.list = list;
    }
}
