package com.mita.wanandroid.http.api;

import com.mita.wanandroid.http.response.CollectInBean;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 收藏站内文章api
 *
 * @author Mita
 * @date 2018/5/5 15:23
 **/
public interface ApiCollectIn {

    @POST
    Call<CollectInBean> requestCollectIn(
            @Url String url);
}
