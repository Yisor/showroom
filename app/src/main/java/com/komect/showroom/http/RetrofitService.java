package com.komect.showroom.http;

import android.support.annotation.NonNull;
import com.komect.showroom.data.request.LoginRequest;
import com.komect.showroom.data.response.BaseResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lsl on 2017/10/7.
 */
public interface RetrofitService {
    /**
     * 获取验证码
     */
    @POST("/allInOne/getVerifyMessage")
    Call<BaseResult> getVerifyMessage(@Query("phoneNum") String phoneNum);

    /**
     * 登录
     */
    @POST("/allInOne/mock/login")
    Call<BaseResult> login(@NonNull @Body LoginRequest param);

    /**
     * 登录
     */
    @POST("/allInOne/appLogin")
    Call<BaseResult> appLogin(@Query("phoneNum") String phoneNum,
                           @Query("verifyCode") String verifyCode);
}
