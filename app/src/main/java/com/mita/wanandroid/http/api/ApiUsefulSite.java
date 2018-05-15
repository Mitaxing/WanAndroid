package com.mita.wanandroid.http.api;

import com.mita.wanandroid.http.response.UsefulSiteBean;
import com.mita.wanandroid.utils.Constant;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiUsefulSite {

    @GET(Constant.API_USEFUL_SITE)
    Call<UsefulSiteBean> getUsefulSite();
}
