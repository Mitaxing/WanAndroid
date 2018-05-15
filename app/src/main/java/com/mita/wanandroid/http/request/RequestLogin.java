package com.mita.wanandroid.http.request;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mita.wanandroid.dao.option.CollectionOption;
import com.mita.wanandroid.http.ApiRequest;
import com.mita.wanandroid.http.api.ApiLogin;
import com.mita.wanandroid.http.response.LoginBean;
import com.mita.wanandroid.http.result.ResultLoginListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 请求登录
 *
 * @author Mita
 * @date 2018/4/9 14:00
 **/
public class RequestLogin {

    private ApiLogin mApiLogin;
    private ResultLoginListener loginListener;

    public RequestLogin(Context context, ResultLoginListener loginListener) {
        this.mApiLogin = ApiRequest.getInstance(context).createLogin(ApiLogin.class);
        this.loginListener = loginListener;
    }

    public void requestLogin(String username, String pwd) {
        Call<LoginBean> request = mApiLogin.requestLogin(username, pwd);
        request.enqueue(new Callback<LoginBean>() {
            @Override
            public void onResponse(@NonNull Call<LoginBean> call, @NonNull Response<LoginBean> response) {
                LoginBean loginBean = response.body();
                if (loginBean == null) {
                    return;
                }
                if (loginBean.getErrorCode() == 0) {
                    if (loginListener != null) {
                        loginListener.loginSuccess();
                    }
                    CollectionOption.saveUserCollectionId(loginBean.getData().getCollectIds());
                } else {
                    if (loginListener != null) {
                        loginListener.loginFail(loginBean.getErrorMsg());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginBean> call, @NonNull Throwable t) {
                if (loginListener != null) {
                    loginListener.loginFail(t.getMessage());
                }
            }
        });
    }
}
