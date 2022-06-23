package com.jess.arms.base;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

public class BaseHandler<T> extends Handler {

    private final WeakReference<T> weak_reference;

    public BaseHandler(T t) {
        weak_reference = new WeakReference<>(t);
    }

    @Override
    public void handleMessage(Message msg) {
        T activity = weak_reference.get();
        if (activity != null) {
            handleMessageWeakActivity(msg, activity);
        }
    }

    public void handleMessageWeakActivity(Message msg, T t) {

    }
}