package com.zqw.mobile.grainfull.mvp.model.entity;

/** 用于EventBus 传输数据 */
public class EventBusEntity {
    public EventBusEntity() {
    }

    public EventBusEntity(long position) {
        this.position = position;
    }

    private long position = 0;

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }
}
