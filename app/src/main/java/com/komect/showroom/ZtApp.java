package com.komect.showroom;

import android.content.Context;
import com.komect.showroom.http.KomectManager;
import com.komect.showroom.http.RetrofitService;

/**
 * Created by eyow on 2017/10/7.
 */

public class ZtApp {
    private static RetrofitService retrofitService;

    /**
     * 初始化
     */
    public static void init(Context context, String httpHost) {
        //初始化API接口
        KomectManager.builder()
                     .setBaseUrl(httpHost)
                     .setCertificates("")
                     .build(context);

        retrofitService = KomectManager.getInstance().getRetrofitService();
    }

    public static RetrofitService getRetrofitService() {
        return retrofitService;
    }

    public static void setRetrofitService(RetrofitService retrofitService) {
        ZtApp.retrofitService = retrofitService;
    }
}
