package com.mita.wanandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mita.wanandroid.R;
import com.mita.wanandroid.adapter.MainHomeAdapter;
import com.mita.wanandroid.entity.Home;
import com.mita.wanandroid.http.request.RequestMyCollection;
import com.mita.wanandroid.http.result.ResultSearchListener;
import com.mita.wanandroid.utils.IToast;
import com.mita.wanandroid.utils.LayoutUtils;
import com.mita.wanandroid.utils.NetworkUtils;
import com.mita.wanandroid.view.SpaceItemDecoration;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的收藏
 *
 * @author Mita
 * @date 2018/4/18 15:53
 **/
public class MyCollectionActivity extends BaseActivity implements ResultSearchListener,
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.RequestLoadMoreListener,
        View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.spl_search)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.rv_search_result)
    RecyclerView mRvSearch;

    private MainHomeAdapter adapter;
    private List<Home> list = new ArrayList<>();

    private int page, totalPage;
    private boolean isLoadMore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_result);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestCollection();
    }

    private void init() {
        mTvTitle.setText(R.string.main_menu_collect);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvSearch.setLayoutManager(linearLayoutManager);
        adapter = new MainHomeAdapter(this, R.layout.adapter_main_home, list);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemChildClickListener(this);
        adapter.openLoadAnimation();
        adapter.setOnLoadMoreListener(this, mRvSearch);
        mRvSearch.addItemDecoration(new SpaceItemDecoration(30));
        mRvSearch.setAdapter(adapter);
    }

    /**
     * 请求搜索内容
     */
    private void requestCollection() {
        mSwipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        if (isLoadMore) {
            ++page;
        } else {
            page = 0;
        }
        if (!NetworkUtils.isConnected(this)) {
            mSwipeRefreshLayout.setRefreshing(false);
            LayoutUtils.showEmptyLayout(this, adapter, false);
        } else {
            new RequestMyCollection(this, this).queryMyCollection(page);
        }
    }

    @Override
    public void requestSearchSuccess(List<Home> articles, int total) {
        totalPage = total;
        if (isLoadMore) {
            int lastPos = list.size() + 1;
            list.addAll(articles);
            adapter.notifyItemInserted(lastPos);
            adapter.loadMoreComplete();
            isLoadMore = !isLoadMore;
        } else {
            list.clear();
            list.addAll(articles);
            adapter.notifyItemChanged(1, articles.size());
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void requestSearchFail(String msg) {
        IToast.showToast(this, msg);
        mSwipeRefreshLayout.setRefreshing(false);
        if (msg.contains(getString(R.string.login))) {
            View convertView = LayoutInflater.from(this).inflate(R.layout.layout_empty_need_login, null);
            //适配
            ScreenAdapterTools.getInstance().loadView((ViewGroup) convertView);
            convertView.findViewById(R.id.btn_to_login).setOnClickListener(this);
            adapter.setEmptyView(convertView);
        } else {
            LayoutUtils.showEmptyLayout(this, adapter, true);
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Home article = list.get(position);
        startWeb(article.getLink(), article.getOriginId());
    }

    @Override
    public void onLoadMoreRequested() {
        if (page >= totalPage) {
            adapter.loadMoreEnd();
            return;
        }
        isLoadMore = true;
        onRefresh();
    }

    /**
     * 打开Web页面
     *
     * @param url url
     */
    private void startWeb(String url, int articleId) {
        Intent intent = new Intent();
        intent.putExtra("url", url);
        intent.putExtra("articleId", articleId);
        startIntentWithData(WebActivity.class, intent);
    }

    @OnClick({R.id.ifv_title_left})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.ifv_title_left:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_to_login:
                startIntent(LoginActivity.class);
                break;

            default:
                break;
        }
    }
}
