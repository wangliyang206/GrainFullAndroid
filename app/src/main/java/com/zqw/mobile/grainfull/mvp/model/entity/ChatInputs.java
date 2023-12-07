package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: ChatInputs
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/12/7 16:38
 */
public class ChatInputs {
    // welcomeText、variables、questionGuide、tts
    private String key;
    private String type;
    // 开场白、对话框变量、问题引导、语音播报
    private String label;
    // 设定的内容
    private Object value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
