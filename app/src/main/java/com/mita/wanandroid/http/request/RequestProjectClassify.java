package com.mita.wanandroid.http.request;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mita.wanandroid.entity.ProjectClassify;
import com.mita.wanandroid.http.ApiRequest;
import com.mita.wanandroid.http.api.ApiProjectClassify;
import com.mita.wanandroid.http.response.ProjectClassifyBean;
import com.mita.wanandroid.http.result.ResultProjectClassifyListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 请求项目分类
 *
 * @author Mita
 * @date 2018/5/8 14:12
 **/
public class RequestProjectClassify {

    private ApiProjectClassify mApiProjectClassify;

    private ResultProjectClassifyListener projectClassifyListener;

    public RequestProjectClassify(Context context, ResultProjectClassifyListener projectClassifyListener) {
        this.projectClassifyListener = projectClassifyListener;
        this.mApiProjectClassify = ApiRequest.getInstance(context).create(ApiProjectClassify.class);
    }

    public void getProjectClassify() {
        Call<ProjectClassifyBean> request = mApiProjectClassify.getProjectClassify();
        request.enqueue(new Callback<ProjectClassifyBean>() {
            @Override
            public void onResponse(@NonNull Call<ProjectClassifyBean> call,
                                   @NonNull Response<ProjectClassifyBean> response) {
                ProjectClassifyBean classifyBean = response.body();
                if (classifyBean == null || projectClassifyListener == null) {
                    return;
                }
                if (classifyBean.getErrorCode() == 0) {
                    List<ProjectClassify> list = convertProjectClassify(classifyBean.getData());
                    projectClassifyListener.onProjectClassifySuccess(list);
                } else {
                    projectClassifyListener.onProjectClassifyFail(classifyBean.getErrorMsg());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProjectClassifyBean> call, @NonNull Throwable t) {
                if (projectClassifyListener != null) {
                    projectClassifyListener.onProjectClassifyFail(t.getMessage());
                }
            }
        });
    }

    private List<ProjectClassify> convertProjectClassify(List<ProjectClassifyBean.DataBean> dataBeanList) {
        List<ProjectClassify> list = new ArrayList<>();
        for (ProjectClassifyBean.DataBean dataBean : dataBeanList) {
            ProjectClassify classify = new ProjectClassify(
                    dataBean.getId(),
                    dataBean.getName()
            );
            list.add(classify);
        }
        return list;
    }
}
