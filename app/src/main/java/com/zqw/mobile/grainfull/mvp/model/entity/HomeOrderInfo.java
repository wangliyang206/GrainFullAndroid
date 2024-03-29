package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * 首页订单
 */
public class HomeOrderInfo {
    public HomeOrderInfo() {
    }

    public HomeOrderInfo(String orderNo, int status, String statusOrder, String storeName, String recycleName, String orgName, double weight, double amount, String recycleTime) {
        this.orderNo = orderNo;
        this.status = status;
        this.statusOrder = statusOrder;
        this.storeName = storeName;
        this.recycleName = recycleName;
        this.orgName = orgName;
        this.weight = weight;
        this.amount = amount;
        this.recycleTime = recycleTime;
    }

    // 订单号
    private String orderNo;
    // 显示状态
    private int status;
    private String statusOrder;
    // 门店
    private String storeId;
    private String storeName;
    // 回收员
    private String recycleId;
    private String recycleName;
    // 回收商
    private String orgId;
    private String orgName;
    // 重量
    private double weight;
    // 订单金额
    private double amount;
    // 收货时间
    private String recycleTime;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(String statusOrder) {
        this.statusOrder = statusOrder;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getRecycleId() {
        return recycleId;
    }

    public void setRecycleId(String recycleId) {
        this.recycleId = recycleId;
    }

    public String getRecycleName() {
        return recycleName;
    }

    public void setRecycleName(String recycleName) {
        this.recycleName = recycleName;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getRecycleTime() {
        return recycleTime;
    }

    public void setRecycleTime(String recycleTime) {
        this.recycleTime = recycleTime;
    }
}
