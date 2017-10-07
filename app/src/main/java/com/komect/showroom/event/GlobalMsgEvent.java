package com.komect.showroom.event;

import org.greenrobot.eventbus.EventBus;

/**
 * 全局消息事件
 * Author by hf
 * Create on 16/8/18
 */
public class GlobalMsgEvent extends BaseEvent {
    /**
     * 发送通用的提示"功能未实现"
     */
    public static void postImplementMsg() {
        EventBus.getDefault().post(new GlobalMsgEvent().setMsg("此功能还未实现"));
    }

    public enum DisplayType {
        Toast, Dialog
    }

    /**
     * 消息显示类型：Toast或者Dialog，默认Toast
     */
    private DisplayType displayType = DisplayType.Toast;
    /**
     * 消息文案
     */
    private String msg;
    /**
     * 是否关闭Dialog，默认为false为显示Dialog
     */
    private boolean closeDialog;

    public String getMsg() {
        return msg;
    }

    public GlobalMsgEvent setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public DisplayType getDisplayType() {
        return displayType;
    }

    public GlobalMsgEvent setDisplayType(DisplayType displayType) {
        this.displayType = displayType;
        return this;
    }

    public boolean isCloseDialog() {
        return closeDialog;
    }

    public GlobalMsgEvent setCloseDialog(boolean closeDialog) {
        this.closeDialog = closeDialog;
        return this;
    }
}
