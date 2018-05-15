package com.mita.wanandroid.http.api;

import com.mita.wanandroid.http.response.SearchHotKeyBean;
import com.mita.wanandroid.utils.Constant;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiSearchHotKey {

    @GET(Constant.API_SEARCH)
    Call<SearchHotKeyBean> getSearchHotKey();
}
