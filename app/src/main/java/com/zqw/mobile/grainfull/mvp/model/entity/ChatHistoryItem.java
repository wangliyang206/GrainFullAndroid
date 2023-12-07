package com.zqw.mobile.grainfull.mvp.model.entity;

import java.util.List;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: ChatHistory
 * @Description:
 * @Author: WLY
 * @CreateDate: 2023/12/7 16:25
 */
public class ChatHistoryItem {
    // 会话id
    private String chatId;
    // 应用id
    private String appId;

    // 标题
    private String title;
    // 历史记录
    private List<ChatHistoryInfo> history;
    // AI提示
    private ChatApp app;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ChatHistoryInfo> getHistory() {
        return history;
    }

    public void setHistory(List<ChatHistoryInfo> history) {
        this.history = history;
    }

    public ChatApp getApp() {
        return app;
    }

    public void setApp(ChatApp app) {
        this.app = app;
    }
}
