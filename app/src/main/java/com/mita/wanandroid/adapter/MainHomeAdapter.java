package com.mita.wanandroid.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mita.wanandroid.R;
import com.mita.wanandroid.entity.Home;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

/**
 * 首页adapter
 *
 * @author Mita
 * @date 2018/3/28 10:16
 **/
public class MainHomeAdapter extends BaseQuickAdapter<Home, BaseViewHolder> {

    private Context context;

    public MainHomeAdapter(Context context, int layoutResId, @Nullable List<Home> data) {
        super(layoutResId, data);
        this.context = context;
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void convert(BaseViewHolder helper, Home item) {
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_author, item.getAuthor())
                .setText(R.id.tv_date, item.getDate())
                .setText(R.id.tv_classify, item.getClassify());

        if (TextUtils.isEmpty(item.getPicUrl())) {
            helper.setGone(R.id.iv_desc,false);
        } else {
            helper.setGone(R.id.iv_desc,true);
            helper.addOnClickListener(R.id.iv_desc);
            Glide.with(context).load(item.getPicUrl()).into((ImageView) helper.getView(R.id.iv_desc));
        }
        String tag = item.getTag();
        String desc = item.getDesc();
        if (TextUtils.isEmpty(tag)) {
            helper.setGone(R.id.tv_tag, false);
        } else {
            helper.setGone(R.id.tv_tag, true);
            helper.setText(R.id.tv_tag, tag);
        }
        if (TextUtils.isEmpty(desc)) {
            helper.setGone(R.id.tv_desc, false);
        } else {
            helper.setGone(R.id.tv_desc, true);
            desc = mContext.getString(R.string.article_desc, desc);
            helper.setText(R.id.tv_desc, desc);
        }
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        View convertView = super.getItemView(layoutResId, parent);
        //适配
        ScreenAdapterTools.getInstance().loadView((ViewGroup) convertView);
        return convertView;
    }
}
