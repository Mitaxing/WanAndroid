package com.mita.wanandroid.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mita.wanandroid.R;
import com.mita.wanandroid.activity.HierarchyActivity;
import com.mita.wanandroid.adapter.MainHierarchyClassifyAdapter;
import com.mita.wanandroid.entity.HierarchyClassify;
import com.mita.wanandroid.http.request.RequestHierarchyClassify;
import com.mita.wanandroid.http.result.ResultHierarchyClassifyListener;
import com.mita.wanandroid.listener.OnHierarchyClassifyClickListener;
import com.mita.wanandroid.utils.IToast;
import com.mita.wanandroid.utils.LayoutUtils;
import com.mita.wanandroid.utils.NetworkUtils;
import com.mita.wanandroid.view.SpaceItemDecoration;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 知识体系fragment
 *
 * @author Mita
 * @date 2018/3/27 16:28
 **/
public class HierarchyFragment extends BaseLazyLoadFragment implements SwipeRefreshLayout.OnRefreshListener,
        ResultHierarchyClassifyListener,
        OnHierarchyClassifyClickListener {

    @BindView(R.id.rv_main_home)
    RecyclerView mRvHome;
    @BindView(R.id.spl_home)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private MainHierarchyClassifyAdapter adapter;

    private List<HierarchyClassify> list = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRvHome.setLayoutManager(linearLayoutManager);
        adapter = new MainHierarchyClassifyAdapter(getActivity(),
                R.layout.adapter_main_hierarchy_classify, list, this);
        adapter.openLoadAnimation();
        mRvHome.addItemDecoration(new SpaceItemDecoration(30));
        mRvHome.setAdapter(adapter);
    }

    @Override
    protected void lazyLoad() {
        mSwipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        if (!NetworkUtils.isConnected(getActivity())) {
            mSwipeRefreshLayout.setRefreshing(false);
            LayoutUtils.showEmptyLayout(getActivity(), adapter, false);
        } else {
            new RequestHierarchyClassify(getActivity(), this).getHierarchyClassify();
        }
    }

    @Override
    public void onHierarchyClassifySuccess(List<HierarchyClassify> classifyList) {
        mSwipeRefreshLayout.setRefreshing(false);
        list.clear();
        list.addAll(classifyList);
        adapter.notifyItemRangeChanged(0, classifyList.size());
    }

    @Override
    public void onHierarchyClassifyFail(String msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        IToast.showToast(getActivity(), msg);
        LayoutUtils.showEmptyLayout(getActivity(), adapter, true);
    }

    @Override
    public void onHierarchyClassifyClick(int parentPos, int tagPos) {
        HierarchyClassify.ChildClassify childClassify = list.get(parentPos).getChildList().get(tagPos);
        Intent intent = new Intent();
        intent.putExtra("classifyName", childClassify.getChildName());
        intent.putExtra("classifyId",childClassify.getChildId());
        startWithIntent(HierarchyActivity.class, intent);
    }
}
