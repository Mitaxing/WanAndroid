package com.mita.wanandroid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mita.wanandroid.R;
import com.mita.wanandroid.entity.About;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.List;

public class AboutAdapter extends BaseMultiItemQuickAdapter<About, BaseViewHolder> {

    private Context context;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public AboutAdapter(Context context, List<About> data) {
        super(data);
        this.context = context;
        addItemType(0, R.layout.item_about_title);
        addItemType(1, R.layout.item_about_content);
        addItemType(2, R.layout.item_about_content);
        addItemType(3, R.layout.item_about_image);
    }

    @Override
    protected void convert(BaseViewHolder helper, About item) {
        switch (helper.getItemViewType()) {
            case 0:
                helper.setText(R.id.tv_about_title, item.getContent());
                break;

            case 1:
                SpannableStringBuilder span = new SpannableStringBuilder("缩进" + item.getContent());
                span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 2,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_about_content, span);
                break;

            case 2:
                span = new SpannableStringBuilder("缩进缩进" + item.getContent());
                span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 4,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                helper.setText(R.id.tv_about_content, span);
                break;

            case 3:
                Glide.with(context).load(item.getImgRes()).into((ImageView) helper.getView(R.id.iv_about_qr));
                break;

            default:
                break;
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
