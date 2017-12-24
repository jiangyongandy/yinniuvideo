package org.cocos2dx.lua.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 功能
 * Created by Jiang on 2017/10/27.
 */

public class Service {
    private static OkHttpClient okHttpClient = null;
    private static WechatApi api;
    private static CommonApi commonApi;
    private static CommonApi commonApi2;

    //todo 对rx引起的内存泄漏进行处理
    public static WechatApi getWeCHatService() {
        if (api == null) {
            initOkHttp();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.baidu.com/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            api = retrofit.create(WechatApi.class);
        }
        return api;
    }

    public static CommonApi getComnonService() {
        if (commonApi == null) {
            initOkHttp();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.baidu.com/")
                    .client(okHttpClient)
                    .addConverterFactory(new StringConverterFactory())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            commonApi = retrofit.create(CommonApi.class);
        }
        return commonApi;
    }

    public static CommonApi getComnonService2() {
        if (commonApi2 == null) {
            initOkHttp();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://www.baidu.com/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            commonApi2 = retrofit.create(CommonApi.class);
        }
        return commonApi2;
    }
    private static void initOkHttp() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
//            }
            //设置超时
            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(20, TimeUnit.SECONDS);
            builder.writeTimeout(20, TimeUnit.SECONDS);
            //错误重连
            builder.retryOnConnectionFailure(true);
            okHttpClient = builder.build();
        }
    }
}
