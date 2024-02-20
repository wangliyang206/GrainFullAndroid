package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: EasyAIAndroid
 * @Package: com.zqw.mobile.easyai.mvp.model.entity
 * @ClassName: LoginFastGptResponse
 * @Description: FastGPT登录
 * @Author: WLY
 * @CreateDate: 2024/2/18 17:17
 */
public class LoginFastGptResponse {
    private int code;
    private String statusText;
    private String message;
    private LoginFastGPT data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginFastGPT getData() {
        return data;
    }

    public void setData(LoginFastGPT data) {
        this.data = data;
    }
}
