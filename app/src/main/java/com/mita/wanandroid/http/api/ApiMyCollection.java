package com.mita.wanandroid.http.api;

import com.mita.wanandroid.http.response.HomeArticleBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiMyCollection {

    @GET
    Call<HomeArticleBean> queryMyCollection(
            @Url String url
    );
}
