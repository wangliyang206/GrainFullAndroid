package com.jess.arms.cj;

/**
 * @Title: ErrorInfo
 * @Package com.cj.mobile.common.model
 * @Description: 网络请求的错误信息
 * @author: 王力杨
 * @date: 16/5/24 上午9:35
 */
public class ErrorInfo {
    // 错误代码（负数表示错误，正数是警告）
    private String errorcode;
    // 错误提示
    private String errormessage = "";

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }
}
