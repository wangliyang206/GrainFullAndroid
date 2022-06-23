package com.jess.arms.cj;

/**
 * @Title: ApiException
 * @Package com.chinamall21.mobile.common.api.exception
 * @Description: Api请求异常信息
 * @author: 赵志军
 * @date: 16/5/24 上午10:52
 */
public class ApiException extends Exception {
    private ErrorInfo errorInfo;

    public ApiException(ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }

    public String getErrorCode() {
        if (errorInfo != null) {
            return errorInfo.getErrorcode();
        } else {
            return ErrorCode.OTHER_ERROR;
        }
    }

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }

    public String getErrorMessage() {
        if (errorInfo != null) {
            return errorInfo.getErrormessage();
        } else {
            return "";
        }
    }
}
