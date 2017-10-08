package com.komect.showroom.data.response;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lsl on 2017/9/7.
 */
public class LoginResult {

    /**
     * sessionId : 4CEDE06141E3D457F7E6C157DA47A4B2
     */

    public String sessionId;


    public String getSessionId() {
        return sessionId;
    }


    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


    /**
     * 将当前对象通过EventBus发送出去
     */
    public void send() {
        EventBus.getDefault().post(this);
    }
}
