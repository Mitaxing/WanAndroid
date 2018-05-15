package com.mita.wanandroid.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mita.wanandroid.R;
import com.mita.wanandroid.entity.HierarchyClassify;
import com.mita.wanandroid.listener.OnHierarchyClassifyClickListener;
import com.yatoooon.screenadaptation.ScreenAdapterTools;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 体系分类adapter
 *
 * @author Mita
 * @date 2018/4/21 11:38
 **/
public class MainHierarchyClassifyAdapter extends BaseQuickAdapter<HierarchyClassify, BaseViewHolder>
        implements TagFlowLayout.OnTagClickListener {

    private Context context;
    private OnHierarchyClassifyClickListener onHierarchyClassifyClickListener;

    public MainHierarchyClassifyAdapter(Context context, int layoutResId,
                                        @Nullable List<HierarchyClassify> data,
                                        OnHierarchyClassifyClickListener onHierarchyClassifyClickListener) {
        super(layoutResId, data);
        this.context = context;
        this.onHierarchyClassifyClickListener = onHierarchyClassifyClickListener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, HierarchyClassify item) {
        helper.setText(R.id.tv_classify_name, item.getClassifyName());

        final TagFlowLayout mFlowLayout = helper.getView(R.id.tag_flow_layout);
        List<HierarchyClassify.ChildClassify> childClassifyList = item.getChildList();
        if (childClassifyList == null) {
            return;
        }
        List<String> tags = new ArrayList<>();
        for (HierarchyClassify.ChildClassify childClassify : childClassifyList) {
            tags.add(childClassify.getChildName());
        }
        mFlowLayout.setAdapter(new TagAdapter<String>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(context)
                        .inflate(R.layout.layout_search_hot_tag, mFlowLayout, false);
                tv.setText(s);
                return tv;
            }
        });
        mFlowLayout.setOnTagClickListener(this);
        mFlowLayout.setTag(helper.getLayoutPosition());
        //适配
        ScreenAdapterTools.getInstance().loadView(mFlowLayout);
    }

    @Override
    protected View getItemView(int layoutResId, ViewGroup parent) {
        View convertView = super.getItemView(layoutResId, parent);
        //适配
        ScreenAdapterTools.getInstance().loadView((ViewGroup) convertView);
        return convertView;
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        if (onHierarchyClassifyClickListener != null) {
            onHierarchyClassifyClickListener.onHierarchyClassifyClick((Integer) parent.getTag(), position);
        }
        return false;
    }
}
