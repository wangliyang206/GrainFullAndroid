package com.jess.arms.cj;

/**
 * @Title: GsonResponse
 * @Package com.cj.mobile.common.model
 * @Description: 网络请求的响应基类
 * @author: 王力杨
 * @date: 16/5/24 上午9:28
 */
public class GsonResponse<T> {
    //响应版本
    private String version = "";
    //错误或警告信息
    private ErrorInfo errorinfo;
    //响应信息
    private T data;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ErrorInfo getErrorinfo() {
        return errorinfo;
    }

    public void setErrorinfo(ErrorInfo errorinfo) {
        this.errorinfo = errorinfo;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
