package com.mita.wanandroid.http;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.mita.wanandroid.entity.Home;
import com.mita.wanandroid.http.response.HomeArticleBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务器返回bean和本地bean转换
 *
 * @author Mita
 * @date 2018/4/18 16:16
 **/
public class Utils {

    @Nullable
    public static List<Home> convertHome(HomeArticleBean article) {
        if (article.getErrorCode() != 0) {
            return null;
        }
        List<Home> list = new ArrayList<>();
        HomeArticleBean.DataBean dataBean = article.getData();
        List<HomeArticleBean.DataBean.DatasBean> dataBeans = dataBean.getDatas();
        int size = dataBeans.size();
        for (int i = 0; i < size; i++) {
            HomeArticleBean.DataBean.DatasBean datasBean = dataBeans.get(i);
            Home home = new Home();
            home.setId(datasBean.getId());
            home.setOriginId(datasBean.getOriginId());
            home.setAuthor(datasBean.getAuthor());
            String classify;
            if (TextUtils.isEmpty(datasBean.getSuperChapterName())) {
                classify = datasBean.getChapterName();
            } else {
                classify = datasBean.getSuperChapterName() + " / " + datasBean.getChapterName();
            }
            home.setClassify(classify);
            home.setDate(datasBean.getNiceDate());
            home.setFresh(datasBean.isFresh());
            home.setLink(datasBean.getLink());
            home.setTitle(datasBean.getTitle());
            home.setDesc(datasBean.getDesc());
            home.setPicUrl(datasBean.getEnvelopePic());
            List<HomeArticleBean.DataBean.DatasBean.TagsBean> tagsBean = datasBean.getTags();
            if (tagsBean != null) {
                int len = tagsBean.size();
                for (int j = 0; j < len; j++) {
                    HomeArticleBean.DataBean.DatasBean.TagsBean tag = tagsBean.get(j);
                    home.setTag(tag.getName());
                }
            }
            list.add(home);
        }
        return list;
    }
}
