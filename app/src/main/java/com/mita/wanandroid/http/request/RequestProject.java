package com.mita.wanandroid.http.request;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mita.wanandroid.entity.Home;
import com.mita.wanandroid.http.ApiRequest;
import com.mita.wanandroid.http.Utils;
import com.mita.wanandroid.http.api.ApiProject;
import com.mita.wanandroid.http.response.HomeArticleBean;
import com.mita.wanandroid.http.response.ProjectBean;
import com.mita.wanandroid.http.result.ResultHomeArticle;
import com.mita.wanandroid.http.result.ResultProjectListener;
import com.mita.wanandroid.http.result.ResultSearchListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestProject {

    private ApiProject mApiProject;

    private ResultSearchListener projectListener;

    public RequestProject(Context context, ResultSearchListener projectListener) {
        this.projectListener = projectListener;
        this.mApiProject = ApiRequest.getInstance(context).create(ApiProject.class);
    }

    public void queryProject(int page, int cid) {
        String url = "http://www.wanandroid.com/project/list/" + page + "/json?cid=" + cid;
        Call<HomeArticleBean> request = mApiProject.queryProject(url);
        request.enqueue(new Callback<HomeArticleBean>() {
            @Override
            public void onResponse(@NonNull Call<HomeArticleBean> call, @NonNull Response<HomeArticleBean> response) {
                HomeArticleBean projectBean = response.body();
                if (projectBean == null || projectListener == null) {
                    return;
                }
                if (projectBean.getErrorCode() == 0) {
                    List<Home> list = Utils.convertHome(projectBean);
                    projectListener.requestSearchSuccess(list, projectBean.getData().getPageCount());
                } else {
                    projectListener.requestSearchFail(projectBean.getErrorMsg());
                }
            }

            @Override
            public void onFailure(@NonNull Call<HomeArticleBean> call, @NonNull Throwable t) {
                if (projectListener != null) {
                    projectListener.requestSearchFail(t.getMessage());
                }
            }
        });
    }

}
