package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: ChatHistoryInfo
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/12/7 16:28
 */
public class ChatHistoryInfo {
    // 会话id
    private String _id;
    // 数据id
    private String dataId;
    // 对象
    private String obj;
    // 对话内容
    private String value;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
