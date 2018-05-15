package com.mita.wanandroid.http.request;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mita.wanandroid.entity.Home;
import com.mita.wanandroid.http.ApiRequest;
import com.mita.wanandroid.http.Utils;
import com.mita.wanandroid.http.api.ApiHierarchy;
import com.mita.wanandroid.http.response.HomeArticleBean;
import com.mita.wanandroid.http.result.ResultSearchListener;
import com.mita.wanandroid.utils.Constant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestHierarchy {

    private ApiHierarchy mApiHierarchy;
    private ResultSearchListener searchListener;

    public RequestHierarchy(Context context, ResultSearchListener searchListener) {
        this.searchListener = searchListener;
        this.mApiHierarchy = ApiRequest.getInstance(context).create(ApiHierarchy.class);
    }

    public void requestHierarchy(int classifyId, int page) {
        String url = Constant.API_BASE + "article/list/" + page + "/json?cid=" + classifyId;
        Call<HomeArticleBean> request = mApiHierarchy.getHierarchy(url);
        request.enqueue(new Callback<HomeArticleBean>() {
            @Override
            public void onResponse(@NonNull Call<HomeArticleBean> call, @NonNull Response<HomeArticleBean> response) {
                HomeArticleBean articleBean = response.body();
                if (articleBean == null || searchListener == null) {
                    return;
                }
                if (articleBean.getErrorCode() == 0) {
                    List<Home> list = Utils.convertHome(articleBean);
                    searchListener.requestSearchSuccess(list, articleBean.getData().getPageCount());
                } else {
                    Log.i("xing", "错误码：" + articleBean.getErrorCode());
                    searchListener.requestSearchFail(articleBean.getErrorMsg());
                }
            }

            @Override
            public void onFailure(@NonNull Call<HomeArticleBean> call, @NonNull Throwable t) {
                if (searchListener != null) {
                    searchListener.requestSearchFail(t.getMessage());
                }
            }
        });
    }
}
