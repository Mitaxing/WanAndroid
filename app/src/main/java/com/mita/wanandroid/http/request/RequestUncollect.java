package com.mita.wanandroid.http.request;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mita.wanandroid.http.ApiRequest;
import com.mita.wanandroid.http.api.ApiUnCollect;
import com.mita.wanandroid.http.response.UncollectBean;
import com.mita.wanandroid.http.result.ResultLoginListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 请求取消收藏文章
 *
 * @author Mita
 * @date 2018/5/7 14:25
 **/
public class RequestUncollect {

    private ApiUnCollect mApiUnCollect;
    private ResultLoginListener resultListener;

    public RequestUncollect(Context context, ResultLoginListener resultListener) {
        this.resultListener = resultListener;
        this.mApiUnCollect = ApiRequest.getInstance(context).create(ApiUnCollect.class);
    }

    public void requestUnCollect(int articleId) {
        String url = "http://www.wanandroid.com/lg/uncollect_originId/" + articleId + "/json";
        Call<UncollectBean> request = mApiUnCollect.requestUncollect(url);
        request.enqueue(new Callback<UncollectBean>() {
            @Override
            public void onResponse(@NonNull Call<UncollectBean> call, @NonNull Response<UncollectBean> response) {
                UncollectBean uncollectBean = response.body();
                if (uncollectBean == null || resultListener == null) {
                    return;
                }
                if (uncollectBean.getErrorCode() == 0) {
                    resultListener.loginSuccess();
                } else {
                    resultListener.loginFail(uncollectBean.getErrorMsg());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UncollectBean> call, @NonNull Throwable t) {
                if (resultListener != null) {
                    resultListener.loginFail(t.getMessage());
                }
            }
        });
    }
}
