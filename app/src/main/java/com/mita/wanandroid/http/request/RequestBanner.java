package com.mita.wanandroid.http.request;

import android.content.Context;

import com.mita.wanandroid.entity.Banner;
import com.mita.wanandroid.http.ApiRequest;
import com.mita.wanandroid.http.api.ApiBanner;
import com.mita.wanandroid.http.response.BannerBean;
import com.mita.wanandroid.http.result.ResultBannerListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 请求首页banner
 *
 * @author Mita
 * @date 2018/3/30 10:58
 **/
public class RequestBanner {

    private ApiBanner mApiBanner;
    private ResultBannerListener bannerListener;

    public RequestBanner(Context context, ResultBannerListener bannerListener) {
        mApiBanner = ApiRequest.getInstance(context).create(ApiBanner.class);
        this.bannerListener = bannerListener;
    }

    /**
     * 请求banner
     */
    public void requestBanner() {
        Call<BannerBean> request = mApiBanner.getBanner();
        request.enqueue(new Callback<BannerBean>() {
            @Override
            public void onResponse(Call<BannerBean> call, Response<BannerBean> response) {
                BannerBean bannerBean = response.body();
                List<Banner> list = covertBanner(bannerBean);
                if (bannerListener != null && list != null && list.size() > 0) {
                    bannerListener.onBannerSuccess(list);
                }
            }

            @Override
            public void onFailure(Call<BannerBean> call, Throwable t) {
            }
        });
    }

    /**
     * 转换banner
     *
     * @param bannerBean 服务器返回Json
     * @return Banner数组
     */
    private List<Banner> covertBanner(BannerBean bannerBean) {
        if (bannerBean == null) {
            return null;
        }
        if (bannerBean.getErrorCode() != 0) {
            return null;
        }
        List<Banner> list = new ArrayList<>();
        List<BannerBean.DataBean> dataBean = bannerBean.getData();
        for (BannerBean.DataBean data : dataBean) {
            Banner banner = new Banner();
            banner.setImagePath(data.getImagePath());
            banner.setTitle(data.getTitle());
            banner.setUrl(data.getUrl());
            list.add(banner);
        }
        return list;
    }
}
