package com.zqw.mobile.grainfull.mvp.model.entity;

import com.huawei.hms.mlsdk.livenessdetection.MLLivenessCaptureResult;

/**
 * 包名： com.zqw.mobile.recycling.model
 * 对象名： MainEvent
 * 描述：首页监听
 * 作者： wly
 * 邮箱：wangliyang206@163.com
 * 创建日期： 2018/3/9 17:52
 */

public class MainEvent {
    public MainEvent() {
    }

    public MainEvent(int code, HomeContentResponse infoResponse) {
        this.code = code;
        this.infoResponse = infoResponse;
    }

    public MainEvent(int code) {
        this.code = code;
    }

    public MainEvent(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public MainEvent(int code, int position) {
        this.code = code;
        this.position = position;
    }


    public MainEvent(int code, int type, int position) {
        this.code = code;
        this.type = type;
        this.position = position;
    }

    public MainEvent(int code, int position, String msg) {
        this.code = code;
        this.position = position;
        this.msg = msg;
    }

    public MainEvent(int code, int type, MLLivenessCaptureResult result) {
        this.code = code;
        this.type = type;
        this.result = result;
    }

    /**
     * 0 = 登录成功；
     * 1 = 打开筛选；
     * 2 = 打开侧滑；
     * 3 = 切换城市；
     * 6 = 签到-切换类型；
     * 7 = 订单-切换类型；
     */
    private int code = 0;
    private String msg = "";
    private int position = 0;
    private int type = 0;
    private HomeContentResponse infoResponse;
    private MLLivenessCaptureResult result;

    public HomeContentResponse getInfoResponse() {
        return infoResponse;
    }

    public void setInfoResponse(HomeContentResponse infoResponse) {
        this.infoResponse = infoResponse;
    }

    public MLLivenessCaptureResult getResult() {
        return result;
    }

    public void setResult(MLLivenessCaptureResult result) {
        this.result = result;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
