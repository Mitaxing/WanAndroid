package com.mita.wanandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.mita.wanandroid.R;
import com.mita.wanandroid.utils.StatusBarUtil;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

/**
 * 基础activity
 *
 * @author Mita
 * @date 2018/3/28 17:24
 **/
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenAdapterTools.getInstance().loadView((ViewGroup) getWindow().getDecorView());
        StatusBarUtil.setStatusBarColor(this, R.color.colorPrimary);
    }

    /**
     * 页面跳转
     *
     * @param clazz 目标页面
     */
    public void startIntent(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * 页面跳转带数据
     *
     * @param clazz  目标页面
     * @param intent Intent
     */
    protected void startIntentWithData(Class clazz, Intent intent) {
        intent.setClass(this, clazz);
        startActivity(intent);
    }
}
