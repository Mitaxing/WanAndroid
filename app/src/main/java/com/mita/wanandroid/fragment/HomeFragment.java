package com.mita.wanandroid.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mita.wanandroid.R;
import com.mita.wanandroid.activity.DragPhotoActivity;
import com.mita.wanandroid.activity.WebActivity;
import com.mita.wanandroid.adapter.MainHomeAdapter;
import com.mita.wanandroid.entity.Home;
import com.mita.wanandroid.glide.GlideImageLoader;
import com.mita.wanandroid.http.request.RequestBanner;
import com.mita.wanandroid.http.request.RequestHomeArticle;
import com.mita.wanandroid.http.result.ResultBannerListener;
import com.mita.wanandroid.http.result.ResultHomeArticle;
import com.mita.wanandroid.utils.LayoutUtils;
import com.mita.wanandroid.utils.NetworkUtils;
import com.mita.wanandroid.view.SpaceItemDecoration;
import com.mita.wanandroid.entity.Banner;
import com.yatoooon.screenadaptation.ScreenAdapterTools;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 主页fragment
 *
 * @author Mita
 * @date 2018/3/27 16:27
 **/
public class HomeFragment extends BaseLazyLoadFragment implements SwipeRefreshLayout.OnRefreshListener,
        OnBannerListener,
        ResultHomeArticle,
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.RequestLoadMoreListener,
        ResultBannerListener {

    @BindView(R.id.rv_main_home)
    RecyclerView mRvHome;
    @BindView(R.id.spl_home)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private MainHomeAdapter adapter;
    private com.youth.banner.Banner banner;

    private List<Home> list = new ArrayList<>();
    private List<String> images = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<String> urls = new ArrayList<>();

    private int page;
    private boolean isLoadMore;
    private boolean hasBanner;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected int setContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void init() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRvHome.setLayoutManager(linearLayoutManager);
        adapter = new MainHomeAdapter(getActivity(), R.layout.adapter_main_home, list);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemChildClickListener(this);
        adapter.openLoadAnimation();
        adapter.setOnLoadMoreListener(this, mRvHome);
        mRvHome.addItemDecoration(new SpaceItemDecoration(30));
        mRvHome.setAdapter(adapter);
        mRvHome.addOnScrollListener(onScrollListener);
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == 0) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int pos = linearLayoutManager.findFirstVisibleItemPosition();
                startOrStopBanner(pos == 0);
            }
        }
    };

    @Override
    protected void lazyLoad() {
        mSwipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        if (isLoadMore) {
            ++page;
        } else {
            page = 0;
            new RequestBanner(getActivity(), this).requestBanner();
        }
        if (!NetworkUtils.isConnected(getActivity())) {
            mSwipeRefreshLayout.setRefreshing(false);
            LayoutUtils.showEmptyLayout(getActivity(), adapter, false);
        } else {
            new RequestHomeArticle(getActivity(), this).requestHomeArticle(page);
        }
    }

    private void initBanner() {
        View view = getLayoutInflater().inflate(R.layout.rv_header_home, (ViewGroup) mRvHome.getParent(), false);

        banner = view.findViewById(R.id.banner_home);
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                //设置图片加载器
                .setImageLoader(new GlideImageLoader())
                //设置banner动画效果
                .setBannerAnimation(Transformer.Default)
                //设置自动轮播，默认为true
                .isAutoPlay(true)
                //设置轮播时间
                .setDelayTime(3000)
                .setOnBannerListener(this)
                //banner设置方法全部调用完毕时最后调用
                .start();
        adapter.addHeaderView(view);
        //滚动到第一个item，显示banner
        mRvHome.smoothScrollToPosition(0);
        hasBanner = true;
    }

    /**
     * 开始或者停止banner轮播
     *
     * @param start 是否开始
     */
    private void startOrStopBanner(boolean start) {
        if (banner == null) {
            return;
        }
        if (start) {
            banner.startAutoPlay();
        } else {
            banner.stopAutoPlay();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        startOrStopBanner(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        startOrStopBanner(false);
    }

    @Override
    public void OnBannerClick(int position) {
        startWeb(urls.get(position), -1);
    }

    @Override
    public void onHomeArticleSuccess(List<Home> articles) {
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
    public void onHomeArticleFail() {
        if (isLoadMore) {
            adapter.loadMoreFail();
            isLoadMore = !isLoadMore;
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        LayoutUtils.showEmptyLayout(getActivity(), adapter, true);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Home article = list.get(position);
        startWeb(article.getLink(), article.getId());
    }

    /**
     * 打开Web页面
     *
     * @param url url
     */
    private void startWeb(String url, int articleId) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("articleId", articleId);
        startActivity(intent);
    }

    @Override
    public void onLoadMoreRequested() {
        isLoadMore = true;
        onRefresh();
    }

    @Override
    public void onResume() {
        super.onResume();
        addBackListener();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            startOrStopBanner(true);
            addBackListener();
        } else {
            startOrStopBanner(false);
            //移除返回监听
            if (getView() != null) {
                getView().setOnKeyListener(null);
            }
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 添加返回监听
     */
    private void addBackListener() {
        if (getView() != null) {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        int pos = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                        if (pos == 0) {
                            lazyLoad();
                        } else {
                            mRvHome.scrollToPosition(0);
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void onBannerSuccess(List<com.mita.wanandroid.entity.Banner> bannerList) {
        images.clear();
        titles.clear();
        urls.clear();
        for (Banner banner : bannerList) {
            images.add(banner.getImagePath());
            titles.add(banner.getTitle());
            urls.add(banner.getUrl());
        }
        if (!hasBanner) {
            initBanner();
        }
        banner.update(images, titles);
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getActivity(), DragPhotoActivity.class);
        int location[] = new int[2];

        view.getLocationOnScreen(location);
        intent.putExtra("left", location[0]);
        intent.putExtra("top", location[1]);
        intent.putExtra("height", view.getHeight());
        intent.putExtra("width", view.getWidth());
        intent.putExtra("url", list.get(position).getPicUrl());
        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);
    }

}