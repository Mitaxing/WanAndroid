package com.mita.wanandroid.http.request;

import android.content.Context;

import com.mita.wanandroid.entity.Home;
import com.mita.wanandroid.http.ApiRequest;
import com.mita.wanandroid.http.Utils;
import com.mita.wanandroid.http.api.ApiHome;
import com.mita.wanandroid.http.response.HomeArticleBean;
import com.mita.wanandroid.http.result.ResultHomeArticle;
import com.mita.wanandroid.utils.Constant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 首页文章请求
 *
 * @author Mita
 * @date 2018/3/28 14:09
 **/
public class RequestHomeArticle {

    private ApiHome mApiHome;

    private ResultHomeArticle resultHomeArticle;

    public RequestHomeArticle(Context context,ResultHomeArticle resultHomeArticle) {
        this.mApiHome = ApiRequest.getInstance(context).create(ApiHome.class);
        this.resultHomeArticle = resultHomeArticle;
    }

    public void requestHomeArticle(int page) {
        String url = Constant.API_BASE + "article/list/" + page + "/json";
        final Call<HomeArticleBean> request = mApiHome.getHomeArticle(url);
        request.enqueue(new Callback<HomeArticleBean>() {
            @Override
            public void onResponse(Call<HomeArticleBean> call, Response<HomeArticleBean> response) {
                HomeArticleBean articleBean = response.body();
                if (articleBean == null) {
                    return;
                }
                List<Home> list = Utils.convertHome(articleBean);
                if (resultHomeArticle != null) {
                    resultHomeArticle.onHomeArticleSuccess(list);
                }
            }

            @Override
            public void onFailure(Call<HomeArticleBean> call, Throwable t) {
                if (resultHomeArticle != null) {
                    resultHomeArticle.onHomeArticleFail();
                }
            }
        });
    }

}
