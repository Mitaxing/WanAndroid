package com.mita.wanandroid.http.api;

import com.mita.wanandroid.http.response.ProjectClassifyBean;
import com.mita.wanandroid.utils.Constant;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiProjectClassify {

    @GET(Constant.API_PROJECT_CLASSIFY)
    Call<ProjectClassifyBean> getProjectClassify();
}
