package com.komect.showroom.event;

import android.os.Bundle;

/**
 * 启动活动（Activity）的事件
 * Author by hf
 * Create on 16/9/20
 */
public final class ActivityStartEvent extends BaseEvent {
    /**
     * 希望启动的目标活动
     */
    private Class<?> targetActivityCls;
    /**
     * 启动新活动时传递的Bundle值
     */
    private Bundle bundle;
    /**
     *启动活动的Intent附带的Flag值
     */
    private int intentFlags;
    /**
     * 启动新活动时，如果希望新活动返回Request结果，则需要设置此requestCode值大于0即可，默认为0
     */
    private int requestCode;
    /**
     *启动新活动时，是否关闭当前活动
     */
    private boolean finishCurrentActivity;
    /**
     * 当前活动关闭返回上一个活动时，调用setResult()所设置的值
     */
    private int result;

    public Class<?> getTargetActivityCls() {
        return targetActivityCls;
    }

    public ActivityStartEvent setTargetActivityCls(Class<?> targetActivityCls) {
        this.targetActivityCls = targetActivityCls;
        return this;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public ActivityStartEvent setBundle(Bundle bundle) {
        this.bundle = bundle;
        return this;
    }

    public int getIntentFlags() {
        return intentFlags;
    }

    public ActivityStartEvent setIntentFlags(int intentFlags) {
        this.intentFlags = intentFlags;
        return this;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public ActivityStartEvent setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public boolean isFinishCurrentActivity() {
        return finishCurrentActivity;
    }

    public ActivityStartEvent setFinishCurrentActivity(boolean finishCurrentActivity) {
        this.finishCurrentActivity = finishCurrentActivity;
        return this;
    }

    public int getResult() {
        return result;
    }

    public ActivityStartEvent setResult(int result) {
        this.result = result;
        return this;
    }
}
