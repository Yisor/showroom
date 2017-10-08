package com.komect.showroom.http;

import android.content.Context;
import android.support.annotation.NonNull;
import com.komect.showroom.event.GlobalMsgEvent;
import com.komect.showroom.util.AppUtil;
import com.komect.showroom.util.EmptyUtils;
import com.komect.showroom.util.NetworkUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by adve on 2016/6/6.
 */
public class KomectManager {
    /**
     * 公开到外部的构造器
     */
    public static class Builder {
        private String baseUrl;
        private String certificatesFileName;
        private SSLSocketFactory sslSocketFactory;
        private OkHttpClient.Builder builder;


        /**
         * 设置后台接口的域名或IP地址
         */
        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }


        /**
         * 导入HTTPS的cer证书文件在assets目录的存放地址
         */
        public Builder setCertificates(String certificatesFileName) {
            this.certificatesFileName = certificatesFileName;
            return this;
        }


        /**
         * 初始化API接口
         *
         * @param application 应用的Application级别的上下文
         */
        public KomectManager build(@NonNull Context application) {
            initSslSocketFactory(application);
            initOkHttpBuilder(application);
            instance = new KomectManager(application, initRetrofitService());
            return instance;
        }


        /**
         * 初始化https证书设置
         */
        private void initSslSocketFactory(Context context) {
            InputStream inputStream = null;
            try {
                if (!EmptyUtils.isEmpty(certificatesFileName)) {
                    inputStream = context.getAssets().open(certificatesFileName);
                    if (inputStream != null) {
                        this.sslSocketFactory = HTTPSTrustManager.buildSSLSocketFactory
                                (inputStream);
                    }
                } else {
                    this.sslSocketFactory = HTTPSTrustManager.allowAllSSLSocketFactory();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        /**
         * 初始化OkHttp设置
         */
        private void initOkHttpBuilder(Context application) {
            //统一拦截设置header
            Interceptor headInterceptor = new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain)
                        throws IOException {
                    Request request = chain.request()
                            .newBuilder()
                            .addHeader("Content-Type", "application/json;charset=UTF-8")
                            .build();
                    return chain.proceed(request);
                }
            };
            builder = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(headInterceptor)
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    });
            if (AppUtil.isApkDebugable(application, application.getPackageName())) {
                builder.addInterceptor(new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY));
            }
            if (sslSocketFactory != null) {
                builder.sslSocketFactory(sslSocketFactory, new HTTPSTrustManager());
            }
        }


        /**
         * 初始化API的接口访问方法
         */
        private RetrofitService initRetrofitService() {
            OkHttpClient okHttpClient = builder.build();
            if (okHttpClient != null) {
                //1.创建Retrofit对象
                Retrofit retrofit = new Retrofit.Builder()
                        .client(okHttpClient)
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                //2.创建访问API的请求
                return retrofit.create(RetrofitService.class);
            } else {
                return null;
            }
        }

    }


    /**
     * 构造器
     */
    public static KomectManager.Builder builder() {
        return new Builder();
    }


    private static KomectManager instance;

    public static synchronized KomectManager getInstance() {
        return instance;
    }

    private final Context mContext;
    private final RetrofitService mRetrofitService;
    private String token;


    private KomectManager(Context context, RetrofitService retrofitService) {
        mContext = context;
        mRetrofitService = retrofitService;
    }


    public synchronized void setToken(String token) {
        this.token = token;
    }


    public synchronized String getToken() {
        return token;
    }


    public RetrofitService getRetrofitService() {
        if (NetworkUtils.isNetworkEnable(mContext)) {
            return mRetrofitService;
        } else {
            new GlobalMsgEvent()
                    .setDisplayType(GlobalMsgEvent.DisplayType.Dialog)
                    .setMsg("无法连接到网络，请稍候再试")
                    .send();
            return null;
        }
    }


    /**
     * 返回动态设置了token和imei数据的header信息，用于retrofit请求
     */
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("token", token);
        return headers;
    }
}
