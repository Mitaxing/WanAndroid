package com.mita.wanandroid.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mita.wanandroid.R;
import com.mita.wanandroid.activity.HierarchyActivity;
import com.mita.wanandroid.activity.WebActivity;
import com.mita.wanandroid.adapter.MainNavigationAdapter;
import com.mita.wanandroid.entity.Navigation;
import com.mita.wanandroid.http.request.RequestNavigation;
import com.mita.wanandroid.http.result.ResultNavigationListener;
import com.mita.wanandroid.listener.OnHierarchyClassifyClickListener;
import com.mita.wanandroid.utils.Constant;
import com.mita.wanandroid.utils.IToast;
import com.mita.wanandroid.utils.LayoutUtils;
import com.mita.wanandroid.utils.NetworkUtils;
import com.mita.wanandroid.view.SpaceItemDecoration;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 导航fragment
 *
 * @author Mita
 * @date 2018/3/27 16:29
 **/
public class NavigationFragment extends BaseLazyLoadFragment implements SwipeRefreshLayout.OnRefreshListener,
        ResultNavigationListener,
        OnHierarchyClassifyClickListener {

    @BindView(R.id.rv_main_home)
    RecyclerView mRvHome;
    @BindView(R.id.spl_home)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private MainNavigationAdapter adapter;

    private List<Navigation> list = new ArrayList<>();

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
        adapter = new MainNavigationAdapter(getActivity(),
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
            new RequestNavigation(getActivity(), this).requestNavigation();
        }
    }

    @Override
    public void onHierarchyClassifyClick(int parentPos, int tagPos) {
        Navigation.Article childClassify = list.get(parentPos).getArticleList().get(tagPos);
        Intent intent = new Intent();
        intent.putExtra("url",childClassify.getLink());
        intent.putExtra("type", Constant.NO_COLLOECT);
        startWithIntent(WebActivity.class, intent);
    }

    @Override
    public void onNavigationSuccess(List<Navigation> navigationList) {
        mSwipeRefreshLayout.setRefreshing(false);
        list.clear();
        list.addAll(navigationList);
        adapter.notifyItemRangeChanged(0, navigationList.size());
    }

    @Override
    public void onNavigationFail(String msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        IToast.showToast(getActivity(), msg);
        LayoutUtils.showEmptyLayout(getActivity(), adapter, true);
    }
}
