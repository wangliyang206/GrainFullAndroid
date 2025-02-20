package com.zqw.mobile.grainfull.mvp.model.entity;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: DetailInfo
 * @Description:
 * @Author: WLY
 * @CreateDate: 2025/2/18 17:00
 */
public class DetailInfo {
    public DetailInfo(String hdzq, String dnyx, List<String> introductionList, List<String> serviceList) {
        this.hdzq = hdzq;
        this.dnyx = dnyx;
        this.introductionList = introductionList;
        this.serviceList = serviceList;
    }

    private String hdzq;
    private String dnyx;
    private List<String> introductionList;
    private List<String> serviceList;

    public String getHdzq() {
        return hdzq;
    }

    public void setHdzq(String hdzq) {
        this.hdzq = hdzq;
    }

    public String getDnyx() {
        return dnyx;
    }

    public void setDnyx(String dnyx) {
        this.dnyx = dnyx;
    }

    public List<String> getIntroductionList() {
        return introductionList;
    }

    public void setIntroductionList(List<String> introductionList) {
        this.introductionList = introductionList;
    }

    public List<String> getServiceList() {
        return serviceList;
    }

    public void setServiceList(List<String> serviceList) {
        this.serviceList = serviceList;
    }
}
