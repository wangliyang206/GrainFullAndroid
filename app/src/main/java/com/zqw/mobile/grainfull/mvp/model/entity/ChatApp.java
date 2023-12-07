package com.zqw.mobile.grainfull.mvp.model.entity;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: ChatApp
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/12/7 16:29
 */
public class ChatApp {
    // FastGPT 应用名称
    private String name;
    // 头像
    private String avatar;
    // 介绍
    private String intro;

    // 模型
    private String[] chatModels;
    // 用户指南模块
    private ChatUserGuideModule userGuideModule;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String[] getChatModels() {
        return chatModels;
    }

    public void setChatModels(String[] chatModels) {
        this.chatModels = chatModels;
    }

    public ChatUserGuideModule getUserGuideModule() {
        return userGuideModule;
    }

    public void setUserGuideModule(ChatUserGuideModule userGuideModule) {
        this.userGuideModule = userGuideModule;
    }
}
