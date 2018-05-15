package com.mita.wanandroid.http.result;

import com.mita.wanandroid.entity.Navigation;

import java.util.List;

public interface ResultNavigationListener {

    void onNavigationSuccess(List<Navigation> navigationList);

    void onNavigationFail(String msg);
}
