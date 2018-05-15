package com.mita.wanandroid.http.api;

import com.mita.wanandroid.http.response.NavigationBean;
import com.mita.wanandroid.utils.Constant;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiNavigation {

    @GET(Constant.API_NAVIGATION)
    Call<NavigationBean> getNavigation();
}
