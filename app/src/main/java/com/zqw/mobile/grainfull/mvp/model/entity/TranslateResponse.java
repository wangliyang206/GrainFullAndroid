package com.zqw.mobile.grainfull.mvp.model.entity;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: TranslateResponse
 * @Description: 翻译 响应结构
 * @Author: WLY
 * @CreateDate: 2023/6/29 16:59
 */
public class TranslateResponse {
    /*-----------------------------------------失败-----------------------------------------*/
    private int error_code = -1;
    private String error_msg;
    /*-----------------------------------------成功-----------------------------------------*/
    private String from;
    private String to;
    private List<TranslateInfo> trans_result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<TranslateInfo> getTrans_result() {
        return trans_result;
    }

    public void setTrans_result(List<TranslateInfo> trans_result) {
        this.trans_result = trans_result;
    }
}
