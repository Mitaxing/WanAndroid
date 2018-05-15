package com.mita.wanandroid.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mita.wanandroid.R;
import com.mita.wanandroid.activity.DragPhotoActivity;
import com.mita.wanandroid.activity.WebActivity;
import com.mita.wanandroid.adapter.MainHomeAdapter;
import com.mita.wanandroid.entity.Home;
import com.mita.wanandroid.entity.ProjectClassify;
import com.mita.wanandroid.http.request.RequestProject;
import com.mita.wanandroid.http.request.RequestProjectClassify;
import com.mita.wanandroid.http.result.ResultProjectClassifyListener;
import com.mita.wanandroid.http.result.ResultSearchListener;
import com.mita.wanandroid.listener.OnProjectFragmentListener;
import com.mita.wanandroid.utils.Constant;
import com.mita.wanandroid.utils.IToast;
import com.mita.wanandroid.utils.LayoutUtils;
import com.mita.wanandroid.utils.NetworkUtils;
import com.mita.wanandroid.utils.ScreenUtils;
import com.mita.wanandroid.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

/**
 * 项目fragment
 *
 * @author Mita
 * @date 2018/3/27 16:29
 **/
public class ProjectFragment extends BaseLazyLoadFragment implements SwipeRefreshLayout.OnRefreshListener,
        ResultProjectClassifyListener,
        BaseQuickAdapter.OnItemClickListener,
        BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.RequestLoadMoreListener,
        PopupWindow.OnDismissListener,
        AdapterView.OnItemClickListener,
        ResultSearchListener {

    @BindView(R.id.rv_main_home)
    RecyclerView mRvHome;
    @BindView(R.id.spl_home)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.view_main)
    View view;

    private MainHomeAdapter adapter;

    private List<Home> list = new ArrayList<>();
    private List<ProjectClassify> classifyList = new ArrayList<>();

    private int page, totalPage, classifyId;
    private boolean isLoadMore, hasClassify;

    private ListPopupWindow listPopupWindow;

    private OnProjectFragmentListener mListener;

    private ClickActionReceiver clickActionReceiver;

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
        adapter = new MainHomeAdapter(getActivity(), R.layout.adapter_main_home, list);
        adapter.setOnItemClickListener(this);
        adapter.setOnItemChildClickListener(this);
        adapter.openLoadAnimation();
        adapter.setOnLoadMoreListener(this, mRvHome);
        mRvHome.addItemDecoration(new SpaceItemDecoration(30));
        mRvHome.setAdapter(adapter);
    }

    @Override
    protected void lazyLoad() {
        onRefresh();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            initReceiver();
        } else {
            if (clickActionReceiver == null || getActivity() == null) {
                return;
            }
            getActivity().unregisterReceiver(clickActionReceiver);
            clickActionReceiver = null;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onRefresh() {
        if (isLoadMore) {
            ++page;
        } else {
            mSwipeRefreshLayout.setRefreshing(true);
            adapter.setOnLoadMoreListener(this, mRvHome);
            adapter.loadMoreComplete();
            page = 1;
        }
        if (!NetworkUtils.isConnected(getActivity())) {
            mSwipeRefreshLayout.setRefreshing(false);
            LayoutUtils.showEmptyLayout(getActivity(), adapter, false);
        } else {
            if (hasClassify) {
                new RequestProject(getActivity(), this).queryProject(page, classifyId);
            } else {
                new RequestProjectClassify(getActivity(), this).getProjectClassify();
            }
        }
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
        if (page >= totalPage) {
            adapter.loadMoreEnd();
            return;
        }
        isLoadMore = true;
        onRefresh();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Intent intent = new Intent(getActivity(), DragPhotoActivity.class);
        int[] location = new int[2];

        view.getLocationOnScreen(location);
        intent.putExtra("left", location[0]);
        intent.putExtra("top", location[1]);
        intent.putExtra("height", view.getHeight());
        intent.putExtra("width", view.getWidth());
        intent.putExtra("url", list.get(position).getPicUrl());
        startActivity(intent);
        Objects.requireNonNull(getActivity()).overridePendingTransition(0, 0);
    }

    @Override
    public void onProjectClassifySuccess(List<ProjectClassify> list) {
        hasClassify = true;
        if (list == null || list.size() == 0) {
            return;
        }
        classifyList.clear();
        classifyList.addAll(list);
        ProjectClassify classify = list.get(0);
        classifyId = classify.getId();
        mListener.onFragmentAction(classify.getName(), true);
        onRefresh();
    }

    @Override
    public void onProjectClassifyFail(String msg) {
        hasClassify = false;
        mSwipeRefreshLayout.setRefreshing(false);
        LayoutUtils.showEmptyLayout(getActivity(), adapter, true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnProjectFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnActivityListener");
        }
    }

    @Override
    public void onDismiss() {
        mListener.onFragmentAction(null, false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ProjectClassify classify = classifyList.get(position);
        listPopupWindow.dismiss();
        if (classify.getId() == classifyId) {
            return;
        }
        classifyId = classify.getId();
        mListener.onFragmentAction(classify.getName(), true);
        isLoadMore = false;
        onRefresh();
    }

    private void initReceiver() {
        clickActionReceiver = new ClickActionReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BROADCAST_TITLE_CLICK);
        Objects.requireNonNull(getActivity()).registerReceiver(clickActionReceiver, filter);
    }

    @Override
    public void requestSearchSuccess(List<Home> articles, int total) {
        Log.i("xing", "文章加载成功：" + articles.size());
        totalPage = total;
        if (isLoadMore) {
            int lastPos = list.size() + 1;
            list.addAll(articles);
            adapter.notifyItemInserted(lastPos);
            isLoadMore = !isLoadMore;
        } else {
            list.clear();
            list.addAll(articles);
            adapter.notifyDataSetChanged();
            mSwipeRefreshLayout.setRefreshing(false);
        }
        adapter.loadMoreComplete();
    }

    @Override
    public void requestSearchFail(String msg) {
        mSwipeRefreshLayout.setRefreshing(false);
        IToast.showToast(getActivity(), msg);
        if (isLoadMore) {
            adapter.loadMoreFail();
        }
        LayoutUtils.showEmptyLayout(getActivity(), adapter, true);
    }

    private class ClickActionReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (listPopupWindow == null) {
                initPop();
            } else {
                listPopupWindow.show();
            }
        }
    }

    /**
     * 初始化ListPopupWindow
     */
    private void initPop() {
        int size = classifyList.size();
        String[] items = new String[size];
        for (int i = 0; i < size; i++) {
            items[i] = classifyList.get(i).getName();
        }
        listPopupWindow = new ListPopupWindow(Objects.requireNonNull(getActivity()));
        // ListView适配器
        listPopupWindow.setAdapter(
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, items));

        // 选择item的监听事件
        listPopupWindow.setOnItemClickListener(this);
        listPopupWindow.setOnDismissListener(this);
        // 对话框的宽高
        int screenWidth = ScreenUtils.getScreenWidth(Objects.requireNonNull(getActivity()));
        int screenHeight = ScreenUtils.getScreenHeight(Objects.requireNonNull(getActivity()));
        listPopupWindow.setWidth(screenWidth * 3 / 5);
        listPopupWindow.setHeight(screenHeight / 2);
        // ListPopupWindow的锚,弹出框的位置是相对当前View的位置
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setModal(true);
        listPopupWindow.show();
    }

}