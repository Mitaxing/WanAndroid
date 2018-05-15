package com.mita.wanandroid.http.api;

import com.mita.wanandroid.http.response.RegisterBean;
import com.mita.wanandroid.utils.Constant;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiRegister {

    @POST(Constant.API_REGISTER)
    Call<RegisterBean> requestRegister(
            @Query("username") String username,
            @Query("password") String password,
            @Query("repassword") String repassword
    );
}
