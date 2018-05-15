package com.mita.wanandroid.http.api;

import com.mita.wanandroid.http.response.LoginBean;
import com.mita.wanandroid.utils.Constant;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiLogin {

    @POST(Constant.API_LOGIN)
    Call<LoginBean> requestLogin(
            @Query("username") String username,
            @Query("password") String password
    );
}
