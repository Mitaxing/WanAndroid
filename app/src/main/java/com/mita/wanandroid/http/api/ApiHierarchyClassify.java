package com.mita.wanandroid.http.api;

import com.mita.wanandroid.http.response.HierarchyClassifyBean;
import com.mita.wanandroid.utils.Constant;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiHierarchyClassify {

    @GET(Constant.API_HIERARCHY_CLASSIFY)
    Call<HierarchyClassifyBean> getHierarchyClassify();
}
