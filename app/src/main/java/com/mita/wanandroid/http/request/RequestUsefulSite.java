package com.mita.wanandroid.http.request;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mita.wanandroid.entity.UsefulSite;
import com.mita.wanandroid.http.ApiRequest;
import com.mita.wanandroid.http.api.ApiUsefulSite;
import com.mita.wanandroid.http.response.UsefulSiteBean;
import com.mita.wanandroid.http.result.ResultUsefulSiteListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestUsefulSite {

    private ApiUsefulSite mApiUsefulSite;

    private ResultUsefulSiteListener usefulSiteListener;

    public RequestUsefulSite(Context context, ResultUsefulSiteListener usefulSiteListener) {
        this.usefulSiteListener = usefulSiteListener;
        this.mApiUsefulSite = ApiRequest.getInstance(context).create(ApiUsefulSite.class);
    }

    public void requestUsefulSite() {
        Call<UsefulSiteBean> request = mApiUsefulSite.getUsefulSite();
        request.enqueue(new Callback<UsefulSiteBean>() {
            @Override
            public void onResponse(@NonNull Call<UsefulSiteBean> call, @NonNull Response<UsefulSiteBean> response) {
                UsefulSiteBean siteBean = response.body();
                if (siteBean == null || usefulSiteListener == null) {
                    return;
                }
                if (siteBean.getErrorCode() == 0) {
                    List<UsefulSite> list = convertUsefulSite(siteBean.getData());
                    usefulSiteListener.onUsefulSiteSuccess(list);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UsefulSiteBean> call, @NonNull Throwable t) {

            }
        });
    }

    private List<UsefulSite> convertUsefulSite(List<UsefulSiteBean.DataBean> dataBeanList) {
        List<UsefulSite> list = new ArrayList<>();
        for (UsefulSiteBean.DataBean dataBean :
                dataBeanList) {
            UsefulSite site = new UsefulSite(dataBean.getId(),
                    dataBean.getLink(), dataBean.getName());
            list.add(site);
        }
        return list;
    }
}
