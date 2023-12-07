package com.zqw.mobile.grainfull.mvp.model.entity;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: UserGuideModule
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/12/7 16:36
 */
public class ChatUserGuideModule {
    // 用户引导
    private String name;
    private String flowType;
    // 包含：开场白、对话框变量、问题引导、语音播报
    private List<ChatInputs> inputs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public List<ChatInputs> getInputs() {
        return inputs;
    }

    public void setInputs(List<ChatInputs> inputs) {
        this.inputs = inputs;
    }
}
