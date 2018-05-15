package com.mita.wanandroid.http.request;

import android.content.Context;
import android.util.Log;

import com.mita.wanandroid.entity.Home;
import com.mita.wanandroid.http.ApiRequest;
import com.mita.wanandroid.http.Utils;
import com.mita.wanandroid.http.api.ApiMyCollection;
import com.mita.wanandroid.http.response.HomeArticleBean;
import com.mita.wanandroid.http.result.ResultSearchListener;
import com.mita.wanandroid.utils.Constant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 请求我的收藏
 *
 * @author Mita
 * @date 2018/4/18 16:15
 **/
public class RequestMyCollection {

    private ApiMyCollection mApiMyCollection;
    private ResultSearchListener searchListener;

    public RequestMyCollection(Context context,ResultSearchListener searchListener) {
        this.mApiMyCollection = ApiRequest.getInstance(context).create(ApiMyCollection.class);
        this.searchListener = searchListener;
    }

    public void queryMyCollection(int page) {
        String url = Constant.API_BASE + "lg/collect/list/+" + page + "/json";
        Call<HomeArticleBean> request = mApiMyCollection.queryMyCollection(url);
        request.enqueue(new Callback<HomeArticleBean>() {
            @Override
            public void onResponse(Call<HomeArticleBean> call, Response<HomeArticleBean> response) {
                HomeArticleBean articleBean = response.body();
                if (articleBean == null || searchListener == null) {
                    return;
                }
                if (articleBean.getErrorCode() == 0) {
                    List<Home> list = Utils.convertHome(articleBean);
                    searchListener.requestSearchSuccess(list, articleBean.getData().getPageCount());
                } else {
                    searchListener.requestSearchFail(articleBean.getErrorMsg());
                }
            }

            @Override
            public void onFailure(Call<HomeArticleBean> call, Throwable t) {
                if (searchListener != null) {
                    searchListener.requestSearchFail(t.getMessage());
                }
            }
        });
    }

}
