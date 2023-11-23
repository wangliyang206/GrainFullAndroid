package com.zqw.mobile.grainfull.mvp.model.entity;

import java.util.List;

/**
 * @author liaoqg
 * @brief description
 * @date 2023-04-13
 */
public class ChatCompletionChunk {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;

    public String getId() {
        return id;
    }

    public String getObject() {
        return object;
    }

    public long getCreated() {
        return created;
    }

    public String getModel() {
        return model;
    }

    public List<Choice> getChoices() {
        return choices;
    }


    public static class Choice {
        private Delta delta;
        private int index;
        private String finish_reason;

        public Delta getDelta() {
            return delta;
        }

        public int getIndex() {
            return index;
        }

        public String getFinishReason() {
            return finish_reason;
        }
    }

    public static class Delta {
        private String content;

        public String getContent() {
            return content;
        }
    }
}
