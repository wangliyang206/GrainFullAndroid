package com.zqw.mobile.grainfull.mvp.model.entity;

import java.util.List;

/**
 * @author liaoqg
 * @brief description
 * @date 2023-04-14
 */
public class ChatImg {

    private long created;
    private List<DataItem> data;

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public List<DataItem> getData() {
        return data;
    }

    public void setData(List<DataItem> data) {
        this.data = data;
    }

    public static class DataItem {
        // 修改后的提示
        private String revised_prompt;
        // 图片链接地址
        private String url;

        public String getRevised_prompt() {
            return revised_prompt;
        }

        public void setRevised_prompt(String revised_prompt) {
            this.revised_prompt = revised_prompt;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
