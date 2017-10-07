package com.komect.showroom.data.response;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lsl on 2017/9/7.
 */
public class LoginResult {
    // TODO: 2017/9/30 添加登录后的返回格式

    /**
     * 将当前对象通过EventBus发送出去
     */
    public void send() {
        EventBus.getDefault().post(this);
    }
}
