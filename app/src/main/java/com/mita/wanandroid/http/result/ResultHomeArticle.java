package com.mita.wanandroid.http.result;

import com.mita.wanandroid.entity.Home;

import java.util.List;

/**
 * 首页文章请求回调
 *
 * @author Mita
 * @date 2018/3/28 15:33
 **/
public interface ResultHomeArticle {

    void onHomeArticleSuccess(List<Home> articles);

    void onHomeArticleFail();
}
