package com.mita.wanandroid.http.request;

import android.content.Context;

import com.mita.wanandroid.entity.Home;
import com.mita.wanandroid.http.ApiRequest;
import com.mita.wanandroid.http.Utils;
import com.mita.wanandroid.http.api.ApiSearch;
import com.mita.wanandroid.http.response.HomeArticleBean;
import com.mita.wanandroid.http.result.ResultSearchListener;
import com.mita.wanandroid.utils.Constant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 查询请求
 *
 * @author Mita
 * @date 2018/4/11 14:47
 **/
public class RequestSearch {

    private ApiSearch mApiSearch;
    private ResultSearchListener searchListener;

    public RequestSearch(Context context, ResultSearchListener searchListener) {
        this.mApiSearch = ApiRequest.getInstance(context).create(ApiSearch.class);
        this.searchListener = searchListener;
    }

    public void requestSearch(int page, String key) {
        String url = Constant.API_BASE + "article/query/+" + page + "/json";
        Call<HomeArticleBean> request = mApiSearch.querySearch(url, key);
        request.enqueue(new Callback<HomeArticleBean>() {
            @Override
            public void onResponse(Call<HomeArticleBean> call, Response<HomeArticleBean> response) {
                HomeArticleBean articleBean = response.body();
                if (articleBean == null || searchListener == null) {
                    return;
                }
                if (articleBean.getErrorCode() == 0) {
                    List<Home> list = Utils.convertHome(articleBean);
                    searchListener.requestSearchSuccess(list, articleBean.getData().getPageCount());
                } else {
                    searchListener.requestSearchFail(articleBean.getErrorMsg());
                }
            }

            @Override
            public void onFailure(Call<HomeArticleBean> call, Throwable t) {
                if (searchListener != null) {
                    searchListener.requestSearchFail(t.getMessage());
                }
            }
        });
    }

}
