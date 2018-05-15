package com.mita.wanandroid.http.result;

import com.mita.wanandroid.entity.Home;

import java.util.List;

public interface ResultSearchListener {

    void requestSearchSuccess(List<Home> list, int total);

    void requestSearchFail(String msg);
}
