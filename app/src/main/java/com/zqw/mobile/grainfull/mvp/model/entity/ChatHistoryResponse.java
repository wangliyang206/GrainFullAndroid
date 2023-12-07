package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: ChatHistory
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/12/7 16:24
 */
public class ChatHistoryResponse {
    private int code;
    private String statusText;
    private String message;
    private ChatHistoryItem data;

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

    public ChatHistoryItem getData() {
        return data;
    }

    public void setData(ChatHistoryItem data) {
        this.data = data;
    }
}
