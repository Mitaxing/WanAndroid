package com.mita.wanandroid.http.request;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mita.wanandroid.entity.Navigation;
import com.mita.wanandroid.http.ApiRequest;
import com.mita.wanandroid.http.api.ApiNavigation;
import com.mita.wanandroid.http.response.NavigationBean;
import com.mita.wanandroid.http.result.ResultNavigationListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 请求导航信息
 *
 * @author Mita
 * @date 2018/5/7 17:34
 **/
public class RequestNavigation {

    private ApiNavigation mApiNavigation;

    private ResultNavigationListener navigationListener;

    public RequestNavigation(Context context, ResultNavigationListener navigationListener) {
        this.navigationListener = navigationListener;
        this.mApiNavigation = ApiRequest.getInstance(context).create(ApiNavigation.class);
    }

    public void requestNavigation() {
        Call<NavigationBean> request = mApiNavigation.getNavigation();
        request.enqueue(new Callback<NavigationBean>() {
            @Override
            public void onResponse(@NonNull Call<NavigationBean> call, @NonNull Response<NavigationBean> response) {
                NavigationBean navigationBean = response.body();
                if (navigationBean == null || navigationListener == null) {
                    return;
                }
                if (navigationBean.getErrorCode() == 0) {
                    List<Navigation> navigationList = convertNavigation(navigationBean.getData());
                    navigationListener.onNavigationSuccess(navigationList);
                } else {
                    navigationListener.onNavigationFail(navigationBean.getErrorMsg());
                }
            }

            @Override
            public void onFailure(@NonNull Call<NavigationBean> call, @NonNull Throwable t) {
                if (navigationListener != null) {
                    navigationListener.onNavigationFail(t.getMessage());
                }
            }
        });
    }

    private List<Navigation> convertNavigation(List<NavigationBean.DataBean> dataBeanList) {
        List<Navigation> list = new ArrayList<>();
        for (NavigationBean.DataBean dataBean : dataBeanList) {
            Navigation navigation = new Navigation();
            int cid = dataBean.getCid();
            String name = dataBean.getName();

            List<Navigation.Article> articleList = new ArrayList<>();
            List<NavigationBean.DataBean.ArticlesBean> articlesBeanList = dataBean.getArticles();
            for (NavigationBean.DataBean.ArticlesBean articlesBean : articlesBeanList) {
                Navigation.Article article = navigation.new Article();
                article.setAuthor(articlesBean.getAuthor());
                article.setChapterId(articlesBean.getChapterId());
                article.setId(articlesBean.getId());
                article.setLink(articlesBean.getLink());
                article.setTitle(articlesBean.getTitle());
                articleList.add(article);
            }
            navigation.setCid(cid);
            navigation.setName(name);
            navigation.setArticleList(articleList);
            list.add(navigation);
        }
        return list;
    }
}
