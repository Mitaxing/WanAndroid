package com.mita.wanandroid.http.result;

import com.mita.wanandroid.entity.Banner;

import java.util.List;

/**
 * Banner请求回调
 *
 * @author Mita
 * @date 2018/3/30 11:53
 **/
public interface ResultBannerListener {

    /**
     * Banner请求成功回调
     *
     * @param bannerList Banner数组
     */
    void onBannerSuccess(List<Banner> bannerList);
}
