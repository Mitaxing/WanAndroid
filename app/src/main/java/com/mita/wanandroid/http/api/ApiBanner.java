package com.mita.wanandroid.http.api;

import com.mita.wanandroid.http.response.BannerBean;
import com.mita.wanandroid.utils.Constant;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 首页banner API
 *
 * @author Mita
 * @date 2018/3/30 10:56
 **/
public interface ApiBanner {

    /**
     * 获取首页banner
     *
     * @return banner
     */
    @GET(Constant.API_HOME_BANNER)
    Call<BannerBean> getBanner();
}
