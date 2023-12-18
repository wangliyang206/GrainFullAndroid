package com.zqw.mobile.grainfull.mvp.model.entity;

import java.util.List;

import retrofit2.http.PUT;

/**
 * @ProjectName: GrainFullAndroid
 * @Package: com.zqw.mobile.grainfull.mvp.model.entity
 * @ClassName: GptChat
 * @Description: 闲聊实体类
 * @Author: WLY
 * @CreateDate: 2023/12/18 16:12
 */
public class GptChat {

    private String chatId;
    // 模型
    private String model;
    // 聊天内容
    private List<ChatMessages> messages;
    private boolean stream;
    private boolean detail;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ChatMessages> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessages> messages) {
        this.messages = messages;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }

    public boolean isDetail() {
        return detail;
    }

    public void setDetail(boolean detail) {
        this.detail = detail;
    }

    public static class ChatMessages {
        public ChatMessages(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public ChatMessages() {
        }

        private String role;
        private String content;

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
