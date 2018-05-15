package com.mita.wanandroid.http;

import android.content.Context;

import com.mita.wanandroid.utils.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ApiRequest
 * 网络请求
 *
 * @author MiTa
 * @date 2017/11/28
 */
public class ApiRequest {

    private static ApiRequest instance;
    /**
     * 默认超时时间
     */
    private final static int DEFAULT_TIMEOUT = 10;

    private Context context;

    /**
     * Constructor
     */
    private ApiRequest(Context context) {
        this.context = context;
    }

    /**
     * sington
     */
    public static ApiRequest getInstance(Context context) {
        if (instance == null) {
            instance = new ApiRequest(context);
        }
        return instance;
    }

    /**
     * create api instance
     *
     * @param service api class
     */
    public <T> T create(Class<T> service) {
        return create(DEFAULT_TIMEOUT, service);
    }

    public <T> T createLogin(Class<T> service) {
        return createLogin(DEFAULT_TIMEOUT, service);
    }

    /**
     * create api instance
     *
     * @param timeout 超时时间
     * @param service api class
     */
    public <T> T create(int timeout, Class<T> service) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (timeout > 0) {
            //设置超时
            builder.connectTimeout(timeout, TimeUnit.SECONDS);
            builder.readTimeout(timeout, TimeUnit.SECONDS);
            builder.writeTimeout(timeout, TimeUnit.SECONDS);
            // 错误重连
            builder.retryOnConnectionFailure(true);
        }
        OkHttpClient client = builder.addInterceptor(new AddCookiesInterceptor(context))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(service);
    }

    /**
     * create api instance
     *
     * @param timeout 超时时间
     * @param service api class
     */
    public <T> T createLogin(int timeout, Class<T> service) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (timeout > 0) {
            //设置超时
            builder.connectTimeout(timeout, TimeUnit.SECONDS);
            builder.readTimeout(timeout, TimeUnit.SECONDS);
            builder.writeTimeout(timeout, TimeUnit.SECONDS);
            // 错误重连
            builder.retryOnConnectionFailure(true);
        }
        OkHttpClient client = builder.addInterceptor(new SaveCookiesInterceptor(context))
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.API_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(service);
    }

}