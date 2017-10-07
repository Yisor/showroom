package com.komect.showroom.event;

import org.greenrobot.eventbus.EventBus;

/**
 * 自带send方法的EventBus事件
 * Author by hf
 * Create on 16/9/26
 */
public abstract class BaseEvent {
    /**
     * 将当前对象通过EventBus发送出去
     */
    public void send() {
        EventBus.getDefault().post(this);
    }
}
