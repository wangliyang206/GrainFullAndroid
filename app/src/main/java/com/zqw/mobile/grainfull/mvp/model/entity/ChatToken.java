package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: ChatToken
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/12/1 11:37
 */
public class ChatToken {
    public ChatToken() {
    }

    public ChatToken(int status, float total, float remaining, float used) {
        this.Status = status;
        this.Total = total;
        this.Remaining = remaining;
        this.Used = used;
    }

    // 状态码，1表示成功。
    private int Status;
    // 令牌的总额度
    private float Total;
    // 剩余的金额
    private float Remaining;
    // 已使用的金额
    private float Used;
    // 错误
    private String Error;

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public float getTotal() {
        return Total;
    }

    public void setTotal(float total) {
        Total = total;
    }

    public float getRemaining() {
        return Remaining;
    }

    public void setRemaining(float remaining) {
        Remaining = remaining;
    }

    public float getUsed() {
        return Used;
    }

    public void setUsed(float used) {
        Used = used;
    }
}
