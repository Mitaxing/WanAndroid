package com.mita.wanandroid.http.request;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mita.wanandroid.http.ApiRequest;
import com.mita.wanandroid.http.api.ApiCollectIn;
import com.mita.wanandroid.http.response.CollectInBean;
import com.mita.wanandroid.http.result.ResultLoginListener;
import com.mita.wanandroid.utils.Constant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestCollectIn {

    private ApiCollectIn mApiCollectIn;
    private ResultLoginListener resultListener;

    public RequestCollectIn(Context context, ResultLoginListener resultListener) {
        this.resultListener = resultListener;
        this.mApiCollectIn = ApiRequest.getInstance(context).create(ApiCollectIn.class);
    }

    public void requestCollectIn(int articleId) {
        String url = Constant.API_BASE + "lg/collect/" + articleId + "/json";
        Call<CollectInBean> request = mApiCollectIn.requestCollectIn(url);
        request.enqueue(new Callback<CollectInBean>() {
            @Override
            public void onResponse(@NonNull Call<CollectInBean> call, @NonNull Response<CollectInBean> response) {
                CollectInBean collectInBean = response.body();
                if (collectInBean == null || resultListener == null) {
                    return;
                }
                if (collectInBean.getErrorCode() == 0) {
                    resultListener.loginSuccess();
                } else {
                    resultListener.loginFail(collectInBean.getErrorMsg());
                }
            }

            @Override
            public void onFailure(@NonNull Call<CollectInBean> call, @NonNull Throwable t) {
                if (resultListener != null) {
                    resultListener.loginFail(t.getMessage());
                }
            }
        });
    }
}
