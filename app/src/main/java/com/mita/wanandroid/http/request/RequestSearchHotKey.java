package com.mita.wanandroid.http.request;

import android.content.Context;

import com.mita.wanandroid.http.ApiRequest;
import com.mita.wanandroid.http.api.ApiSearchHotKey;
import com.mita.wanandroid.http.response.SearchHotKeyBean;
import com.mita.wanandroid.http.result.ResultSearchHotKeyListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 请求搜索热词
 *
 * @author Mita
 * @date 2018/4/20 15:55
 **/
public class RequestSearchHotKey {

    private ApiSearchHotKey mApiSearchHotKey;

    private ResultSearchHotKeyListener searchHotKeyListener;

    public RequestSearchHotKey(Context context, ResultSearchHotKeyListener searchHotKeyListener) {
        this.mApiSearchHotKey = ApiRequest.getInstance(context).create(ApiSearchHotKey.class);
        this.searchHotKeyListener = searchHotKeyListener;
    }

    public void requestSearchHotKey() {
        Call<SearchHotKeyBean> request = mApiSearchHotKey.getSearchHotKey();
        request.enqueue(new Callback<SearchHotKeyBean>() {
            @Override
            public void onResponse(Call<SearchHotKeyBean> call, Response<SearchHotKeyBean> response) {
                SearchHotKeyBean searchHotKeyBean = response.body();
                if (searchHotKeyBean == null || searchHotKeyListener == null) {
                    return;
                }
                if (searchHotKeyBean.getErrorCode() == 0) {
                    List<String> list = resolveTags(searchHotKeyBean.getData());
                    searchHotKeyListener.requestSearchHotKeySuccess(list);
                } else {
                    searchHotKeyListener.requestSearchHotKeyFail(searchHotKeyBean.getErrorMsg());
                }
            }

            @Override
            public void onFailure(Call<SearchHotKeyBean> call, Throwable t) {
                if (searchHotKeyListener != null) {
                    searchHotKeyListener.requestSearchHotKeyFail(t.getMessage());
                }
            }
        });
    }

    private List<String> resolveTags(List<SearchHotKeyBean.DataBean> dataBean) {
        List<String> list = new ArrayList<>();
        for (SearchHotKeyBean.DataBean data : dataBean) {
            list.add(data.getName());
        }
        return list;
    }
}
