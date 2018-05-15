package com.mita.wanandroid.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mita.wanandroid.R;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

public class LayoutUtils {

    /**
     * 显示空布局
     *
     * @param context    Context
     * @param adapter    BaseQuickAdapter
     * @param isLoadFail true：加载失败，false：无网络
     */
    public static void showEmptyLayout(Context context, BaseQuickAdapter adapter, boolean isLoadFail) {
        View convertView = LayoutInflater.from(context).inflate(
                isLoadFail ? R.layout.layout_empty_load_fail : R.layout.layout_empty_no_network,
                null);
        //适配
        ScreenAdapterTools.getInstance().loadView((ViewGroup) convertView);
        adapter.setEmptyView(convertView);
    }
}
