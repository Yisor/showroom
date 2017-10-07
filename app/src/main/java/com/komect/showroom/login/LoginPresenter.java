package com.komect.showroom.login;

import android.content.Intent;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.komect.showroom.MainActivity;
import com.komect.showroom.WebActivity;
import com.komect.showroom.ZtApp;
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


    /**
     * 获取验证码
     *
     * @param login
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
                               VerifyMessageResult verifyMessageResult = new Gson().fromJson(result, VerifyMessageResult.class);
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
     *
     * @param login
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
        new ActivityStartEvent()
                .setIntentFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .setFinishCurrentActivity(true)
                .setTargetActivityCls(WebActivity.class)
                .send();

        ZtApp.getRetrofitService().login(login.getPhone(), login.getPassword())
                   .enqueue(new Callback<BaseResult>() {
                       @Override
                       public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                           JsonObject result = HttpResponseHandler.handleResponse(response.body());
                           if (result != null) {
                               LoginResult loginResult = new Gson().fromJson(result, LoginResult.class);

                               // 界面跳转
                               new ActivityStartEvent()
                                       .setIntentFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                       .setFinishCurrentActivity(true)
                                       .setTargetActivityCls(MainActivity.class)
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
