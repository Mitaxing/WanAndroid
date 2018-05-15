package com.mita.wanandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mita.wanandroid.R;
import com.mita.wanandroid.entity.UsefulSite;
import com.mita.wanandroid.http.request.RequestSearchHotKey;
import com.mita.wanandroid.http.request.RequestUsefulSite;
import com.mita.wanandroid.http.result.ResultSearchHotKeyListener;
import com.mita.wanandroid.http.result.ResultUsefulSiteListener;
import com.mita.wanandroid.utils.Constant;
import com.mita.wanandroid.utils.IToast;
import com.yatoooon.screenadaptation.ScreenAdapterTools;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索页面
 *
 * @author Mita
 * @date 2018/4/11 10:54
 **/
public class SearchActivity extends BaseActivity implements ResultSearchHotKeyListener,
        ResultUsefulSiteListener,
        TagFlowLayout.OnTagClickListener {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.tv_hot_key)
    TextView mTvHotKey;
    @BindView(R.id.tfl_hot_key)
    TagFlowLayout mTflHotKey;
    @BindView(R.id.tv_useful_site)
    TextView mTvUsefulSite;
    @BindView(R.id.tfl_useful_site)
    TagFlowLayout mTflUsefulSite;

    private List<String> keys = new ArrayList<>();
    private List<String> sites = new ArrayList<>();

    private List<UsefulSite> siteList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        new RequestSearchHotKey(this, this).requestSearchHotKey();
        new RequestUsefulSite(this, this).requestUsefulSite();
    }

    /**
     * 初始化搜索热词
     */
    private void initHotKeys() {
        mTflHotKey.setAdapter(new TagAdapter<String>(keys) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(SearchActivity.this)
                        .inflate(R.layout.layout_search_hot_tag, mTflHotKey, false);
                tv.setText(s);
                return tv;
            }
        });
        mTflHotKey.setOnTagClickListener(this);
        //适配
        ScreenAdapterTools.getInstance().loadView(mTflHotKey);
    }

    /**
     * 初始化常用网站
     */
    private void initUsefulSites() {
        mTflUsefulSite.setAdapter(new TagAdapter<String>(sites) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(SearchActivity.this)
                        .inflate(R.layout.layout_search_hot_tag, mTflUsefulSite, false);
                tv.setText(s);
                return tv;
            }
        });
        mTflUsefulSite.setOnTagClickListener(tagClickListener);
        //适配
        ScreenAdapterTools.getInstance().loadView(mTflUsefulSite);
    }

    /**
     * 跳转搜索结果页面
     *
     * @param key 搜索内容
     */
    private void startSearch(String key) {
        Intent intent = new Intent();
        intent.putExtra("key", key);
        startIntentWithData(SearchResultActivity.class, intent);
    }

    TagFlowLayout.OnTagClickListener tagClickListener = new TagFlowLayout.OnTagClickListener() {

        @Override
        public boolean onTagClick(View view, int position, FlowLayout parent) {
            if (siteList.get(position) == null) {
                return false;
            }
            String link = siteList.get(position).getLink();
            Intent intent = new Intent();
            intent.putExtra("url", link);
            intent.putExtra("type", Constant.NO_COLLOECT);
            startIntentWithData(WebActivity.class, intent);
            return false;
        }
    };

    @Override
    public void requestSearchHotKeySuccess(List<String> list) {
        keys.addAll(list);
        initHotKeys();
    }

    @Override
    public void requestSearchHotKeyFail(String msg) {
        IToast.showToast(this, msg);
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        startSearch(keys.get(position));
        return true;
    }

    @OnClick({R.id.ifv_title_left, R.id.tv_search})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.ifv_title_left:
                finish();
                break;

            case R.id.tv_search:
                String key = mEtSearch.getText().toString();
                if (!TextUtils.isEmpty(key)) {
                    startSearch(key);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onUsefulSiteSuccess(List<UsefulSite> list) {
        siteList.addAll(list);
        int size = siteList.size();
        if (size > 9) {
            size = 9;
        }
        for (int i = 0; i < size; i++) {
            sites.add(siteList.get(i).getName());
        }
        initUsefulSites();
    }
}