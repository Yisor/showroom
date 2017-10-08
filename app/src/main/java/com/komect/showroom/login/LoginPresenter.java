package com.komect.showroom.login;

import android.content.Intent;
import android.os.Bundle;
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
import com.komect.showroom.util.StringUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lsl on 2017/9/30.
 */

public class LoginPresenter {

    private String verifyCode = null;
    public static final String BUNDLE_SESSION_ID = "sessionId";


    /**
     * 获取验证码
     */
    public void onGetCodeClick(final LoginBean login) {

        if (!StringUtil.isMobile(login.getPhone())) {
            new GlobalMsgEvent().setMsg("请填写正确的手机号").send();
            return;
        }
        // TODO: 2017/9/30 登录的http请求
        new GlobalMsgEvent().setMsg("获取验证码成功").send();

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
    public void onLoginClick(LoginBean login) {
        if (!StringUtil.isMobile(login.getPhone())) {
            new GlobalMsgEvent().setMsg("请填写正确的手机号").send();
            return;
        }
        //if (EmptyUtils.isEmpty(verifyCode)) {
        //    new GlobalMsgEvent().setMsg("请获取验证码").send();
        //    return;
        //}

        // 界面跳转
        //new ActivityStartEvent()
        //        .setIntentFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        //        .setFinishCurrentActivity(true)
        //        .setTargetActivityCls(WebActivity.class)
        //        .send();

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
