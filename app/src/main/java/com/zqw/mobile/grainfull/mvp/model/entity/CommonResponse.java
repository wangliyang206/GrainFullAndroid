package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * 包名： com.zqw.mobile.recycling.model
 * 对象名： CommonResponse
 * 描述：通用响应
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2017/3/28 17:52
 */

public class CommonResponse {
    public CommonResponse() {
    }

    public CommonResponse(String applymentId, String businessCode) {
        this.applymentId = applymentId;
        this.businessCode = businessCode;
    }

    public CommonResponse(int succ, String id) {
        this.succ = succ;
        this.id = id;
    }

    /*1表示成功，0表示失败*/
    private int succ = 0;
    private int result = 0;

    /*Y：成功;N失败*/
    private String code = "";
    // ishaveUser为0时此字段为空字符串，为空字符串是显示默认的提示信息
    private String msg = "";
    private String msgCode = "";

    // 验证通过后返回的标识码(验证短信)
    private String flagCode = "";

    // 业务id
    private String id = "";
    // 拜访记录id
    private String customerVisitId;
    // 维护信息id
    private String maintId;
    // 门店id(进件时用到)
    private String storeId;
    // 是否进件（0：无门店信息；1：已有进件信息但未提交；2：已门店信息并且已提交，不再调用进件接口）
    private int ishaveUser;

    // 微信支付申请单号(特约商户进件用到了)
    private String applymentId;
    // 业务申请编号(特约商户进件用到了)
    private String businessCode;

    public int getIshaveUser() {
        return ishaveUser;
    }

    public void setIshaveUser(int ishaveUser) {
        this.ishaveUser = ishaveUser;
    }

    public String getApplymentId() {
        return applymentId;
    }

    public void setApplymentId(String applymentId) {
        this.applymentId = applymentId;
    }

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getMaintId() {
        return maintId;
    }

    public void setMaintId(String maintId) {
        this.maintId = maintId;
    }

    public String getCustomerVisitId() {
        return customerVisitId;
    }

    public void setCustomerVisitId(String customerVisitId) {
        this.customerVisitId = customerVisitId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getSucc() {
        return succ;
    }

    public void setSucc(int succ) {
        this.succ = succ;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getFlagCode() {
        return flagCode;
    }

    public void setFlagCode(String flagCode) {
        this.flagCode = flagCode;
    }
}
