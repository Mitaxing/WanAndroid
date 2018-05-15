package com.mita.wanandroid.http.api;

import com.mita.wanandroid.http.response.UncollectBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiUnCollect {

    @POST
    Call<UncollectBean> requestUncollect(
            @Url String url
    ) ;
}
