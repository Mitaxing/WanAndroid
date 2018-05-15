package com.mita.wanandroid.http.request;

import android.content.Context;

import com.mita.wanandroid.http.ApiRequest;
import com.mita.wanandroid.http.api.ApiRegister;
import com.mita.wanandroid.http.response.RegisterBean;
import com.mita.wanandroid.http.result.ResultRegisterListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestRegister {

    private ApiRegister mApiRegister;

    private ResultRegisterListener registerListener;

    public RequestRegister(Context context, ResultRegisterListener registerListener) {
        mApiRegister = ApiRequest.getInstance(context).create(ApiRegister.class);
        this.registerListener = registerListener;
    }

    public void requestRegister(String username, String pwd) {
        Call<RegisterBean> request = mApiRegister.requestRegister(username, pwd, pwd);
        request.enqueue(new Callback<RegisterBean>() {
            @Override
            public void onResponse(Call<RegisterBean> call, Response<RegisterBean> response) {
                RegisterBean registerBean = response.body();
                if (registerBean == null || registerListener == null) {
                    return;
                }
                if (registerBean.getErrorCode() == 0) {
                    registerListener.registerSuccess();
                } else {
                    registerListener.registerFail(registerBean.getErrorMsg());
                }
            }

            @Override
            public void onFailure(Call<RegisterBean> call, Throwable t) {
                if (registerListener != null) {
                    registerListener.registerFail(t.getMessage());
                }
            }
        });
    }
}
