package com.mita.wanandroid.http.api;

import com.mita.wanandroid.http.response.HomeArticleBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiSearch {

    @POST
    Call<HomeArticleBean> querySearch(
            @Url String url,
            @Query("k") String key
    );
}
