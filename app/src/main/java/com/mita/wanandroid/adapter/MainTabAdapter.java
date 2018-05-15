package com.mita.wanandroid.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hjhrq991.screenadapter.ScreenAdaperHelper;
import com.mita.wanandroid.R;
import com.mita.wanandroid.entity.HomeTab;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

/**
 * 首页tab adapter
 *
 * @author Mita
 * @date 2018/3/27 11:53
 **/
public class MainTabAdapter extends BaseQuickAdapter<HomeTab, BaseViewHolder> {

    public MainTabAdapter(int layoutResId, @Nullable List<HomeTab> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeTab item) {
        helper.setText(R.id.ifv_tab_icon, item.getImgRes());
        helper.setText(R.id.tv_tab_name, item.getName());
        if (item.isFocus()) {
            helper.setTextColor(R.id.ifv_tab_icon, Color.BLUE);
            helper.setTextColor(R.id.tv_tab_name, Color.BLUE);
        } else {
            helper.setTextColor(R.id.ifv_tab_icon, Color.parseColor("#b2b2b2"));
            helper.setTextColor(R.id.tv_tab_name, Color.parseColor("#b2b2b2"));
        }
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        View convertView = super.getItemView(layoutResId, parent);
        ScreenAdapterTools.getInstance().loadView((ViewGroup) convertView);
        return convertView;
    }
}
