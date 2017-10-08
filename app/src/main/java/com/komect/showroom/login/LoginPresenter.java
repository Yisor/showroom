package com.komect.showroom.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.komect.showroom.WebActivity;
import com.komect.showroom.ZtApp;
import com.komect.showroom.data.request.LoginRequest;
import com.komect.showroom.data.response.BaseResult;
import com.komect.showroom.data.response.LoginResult;
import com.komect.showroom.data.response.VerifyMessageResult;
import com.komect.showroom.event.ActivityStartEvent;
import com.komect.showroom.event.GlobalMsgEvent;
import com.komect.showroom.http.HttpResponseHandler;
import com.komect.showroom.util.SimpleCacheUtil;
import com.komect.showroom.util.StringUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lsl on 2017/9/30.
 */

public class LoginPresenter {
    public static final String BUNDLE_SESSION_ID = "sessionId";
    public static final String BUNDLE_MOBILE = "mobile";

    private String verifyCode = null;
    private SimpleCacheUtil cacheUtil;
    private LoginActivity mLoginActivity;


    public void bindView(LoginActivity loginActivity) {
        mLoginActivity = loginActivity;
    }


    public SimpleCacheUtil getCacheUtil() {
        return cacheUtil;
    }


    public void setCacheUtil(SimpleCacheUtil cacheUtil) {
        this.cacheUtil = cacheUtil;
    }


    /**
     * 获取验证码
     */
    public void onGetCodeClick(final LoginBean login) {

        if (!StringUtil.isMobile(login.getPhone())) {
            new GlobalMsgEvent().setMsg("请填写正确的手机号").send();
            return;
        }
        mLoginActivity.onTimeStart();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                // TODO: 2017/9/30 登录的http请求
                new GlobalMsgEvent().setMsg("获取验证码成功").send();

                mLoginActivity.onCancel();
            }
        }, 1000 * 5);


        ZtApp.getRetrofitService().getVerifyMessage(login.getPhone())
             .enqueue(new Callback<BaseResult>() {
                 @Override
                 public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                     JsonObject result = HttpResponseHandler.handleResponse(response.body());
                     if (result != null) {
                         VerifyMessageResult verifyMessageResult = new Gson().fromJson(result,
                                 VerifyMessageResult.class);
                         verifyCode = verifyMessageResult.getVerifyCode();
                     }
                 }


                 @Override
                 public void onFailure(Call<BaseResult> call, Throwable t) {
                     Log.d("onFailure", "onFailure: " + t.getMessage());
                     new GlobalMsgEvent().setMsg("服务器响应异常").send();
                 }
             });
    }


    /**
     * 登录
     */
    public void onLoginClick(final LoginBean login) {
        if (!StringUtil.isMobile(login.getPhone())) {
            new GlobalMsgEvent().setMsg("请填写正确的手机号").send();
            return;
        }
        //if (EmptyUtils.isEmpty(verifyCode)) {
        //    new GlobalMsgEvent().setMsg("请获取验证码").send();
        //    return;
        //}

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPhoneNumber(login.getPhone());
        loginRequest.setVerifyCode(login.getPassword());
        ZtApp.getRetrofitService().login(loginRequest)
             .enqueue(new Callback<BaseResult>() {
                 @Override
                 public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                     if (response.body() == null) {
                         return;
                     }
                     JsonObject result = HttpResponseHandler.handleResponse(response.body());
                     if (result != null) {
                         LoginResult loginResult = new Gson().fromJson(result, LoginResult.class);
                         Log.d("tlog", "onResponse: " + loginResult.getSessionId());

                         Bundle bundle = new Bundle();
                         bundle.putString(BUNDLE_SESSION_ID, loginResult.getSessionId());
                         bundle.putString(BUNDLE_MOBILE, login.getPhone());
                         // 界面跳转
                         new ActivityStartEvent()
                                 .setIntentFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                 .setBundle(bundle)
                                 .setFinishCurrentActivity(true)
                                 .setTargetActivityCls(WebActivity.class)
                                 .send();
                     }
                 }


                 @Override
                 public void onFailure(Call<BaseResult> call, Throwable t) {
                     Log.d("onFailure", "onFailure: " + t.getMessage());
                     new GlobalMsgEvent().setMsg("服务器响应异常").send();
                 }
             });
    }
}
