package com.komect.showroom.http;

import com.komect.showroom.data.response.BaseResult;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lsl on 2017/10/7.
 */
public interface RetrofitService {
    /**
     * 获取验证码
     */
    @POST("/getVerifyMessage")
    Call<BaseResult> getVerifyMessage(@Query("phoneNum") String phoneNum);

    /**
     * 登录
     */
    @POST("/mock/login")
    Call<BaseResult> login(@Query("phoneNumber") String phoneNum,
                              @Query("verifyCode") String verifyCode);
}
