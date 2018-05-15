package com.mita.wanandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.mita.wanandroid.R;
import com.mita.wanandroid.adapter.FragAdapter;
import com.mita.wanandroid.adapter.MainTabAdapter;
import com.mita.wanandroid.entity.HomeTab;
import com.mita.wanandroid.fragment.HierarchyFragment;
import com.mita.wanandroid.fragment.HomeFragment;
import com.mita.wanandroid.fragment.NavigationFragment;
import com.mita.wanandroid.fragment.ProjectFragment;
import com.mita.wanandroid.http.SaveCookiesInterceptor;
import com.mita.wanandroid.http.request.RequestLogin;
import com.mita.wanandroid.listener.OnProjectFragmentListener;
import com.mita.wanandroid.utils.Constant;
import com.mita.wanandroid.utils.SpUtils;
import com.mita.wanandroid.view.IconFontView;
import com.yatoooon.screenadaptation.ScreenAdapterTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页
 *
 * @author mita
 * @date 2018/3/27
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        BaseQuickAdapter.OnItemClickListener,
        ViewPager.OnPageChangeListener,
        View.OnClickListener,
        OnProjectFragmentListener {

    @BindView(R.id.vp_main_contain)
    ViewPager mVpContain;
    @BindView(R.id.rv_main_tab)
    RecyclerView mRvTab;
    @BindView(R.id.ifv_title_left)
    IconFontView mIfvMenu;
    @BindView(R.id.ifv_title_right)
    IconFontView mIfvSearch;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.ll_middle_title)
    LinearLayout mFlMiddleTitle;
    @BindView(R.id.tv_middle_title)
    TextView mTvMiddleTitle;
    @BindView(R.id.ifv_expand)
    IconFontView mIfvExpand;

    private TextView mTvHead, mTvUsername;

    private MainTabAdapter adapter;
    private List<HomeTab> tabList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private int lastFocusTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //适配
        ScreenAdapterTools.getInstance().loadView((ViewGroup) getWindow().getDecorView());
        ButterKnife.bind(this);
        initViews();
        initTabs();
        initViewPager();
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        fragmentList.add(new HomeFragment());
        fragmentList.add(new HierarchyFragment());
        fragmentList.add(new NavigationFragment());
        fragmentList.add(new ProjectFragment());
        FragAdapter fragAdapter = new FragAdapter(getSupportFragmentManager(), fragmentList);
        mVpContain.setOffscreenPageLimit(4);
        mVpContain.setAdapter(fragAdapter);
        mVpContain.addOnPageChangeListener(this);
    }

    /**
     * 初始化Tab
     */
    private void initTabs() {
        tabList.add(new HomeTab(R.string.icon_tab_home, R.string.main_tab_home, true));
        tabList.add(new HomeTab(R.string.icon_tab_hierarchy, R.string.main_tab_hierarchy, false));
        tabList.add(new HomeTab(R.string.icon_tab_navigation, R.string.main_tab_navigation, false));
        tabList.add(new HomeTab(R.string.icon_tab_project, R.string.main_tab_project, false));

        mRvTab.setLayoutManager(new GridLayoutManager(this, 4) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        adapter = new MainTabAdapter(R.layout.adapter_main_tab, tabList);
        adapter.setOnItemClickListener(this);
        mRvTab.setAdapter(adapter);
    }

    /**
     * 初始化Toolbar、NavigationView
     */
    private void initViews() {
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        View headView = navigationView.getHeaderView(0);
        mTvHead = headView.findViewById(R.id.tv_head);
        mTvUsername = headView.findViewById(R.id.tv_username);
        mTvHead.setOnClickListener(this);

        mIfvMenu.setText(R.string.icon_menu);
        mIfvSearch.setText(R.string.icon_search);
        mIfvSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_collect) {
            Intent intent = new Intent(this, MyCollectionActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
//        } else if (id == R.id.nav_modify_pwd) {

        } else if (id == R.id.nav_logout) {
            //退出登录
            mTvUsername.setText(R.string.tip_no_login);
            mTvHead.setText(R.string.tip_no_login_name);
            mTvTitle.setText(R.string.app_name);
            SpUtils.clearLoginInfo(this);
            SaveCookiesInterceptor.clearCookie(this);
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        changeTab(position);
        mVpContain.setCurrentItem(position, false);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        changeTab(position);
        if (position == fragmentList.size() - 1) {
            mTvTitle.setVisibility(View.GONE);
            mFlMiddleTitle.setVisibility(View.VISIBLE);
        } else {
            mTvTitle.setVisibility(View.VISIBLE);
            mFlMiddleTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void changeTab(int position) {
        if (lastFocusTab == position) {
            return;
        }
        tabList.get(lastFocusTab).setFocus(false);
        adapter.notifyItemChanged(lastFocusTab);
        tabList.get(position).setFocus(true);
        adapter.notifyItemChanged(position);
        lastFocusTab = position;
    }

    @OnClick({R.id.ifv_title_left, R.id.ifv_title_right, R.id.ll_middle_title})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.ifv_title_right:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;

            case R.id.ifv_title_left:
                drawer.openDrawer(Gravity.START);
                break;

            case R.id.ll_middle_title:
                intent = new Intent(Constant.BROADCAST_TITLE_CLICK);
                sendBroadcast(intent);

                mIfvExpand.setText(R.string.icon_up);
                break;

            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head:
                if (TextUtils.isEmpty(SpUtils.getLoginName(this))) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String loginName = SpUtils.getLoginName(this);
        if (!TextUtils.isEmpty(loginName)) {
            mTvTitle.setText(loginName);
            mTvUsername.setText(loginName);
            mTvHead.setText(loginName.substring(0, 1));
        } else {
            mTvUsername.setText(R.string.tip_no_login);
            mTvHead.setText(R.string.tip_no_login_name);
            mTvTitle.setText(R.string.app_name);
        }
    }

    @Override
    public void onFragmentAction(String title, boolean hasSelect) {
        if (hasSelect) {
            mTvMiddleTitle.setText(title);
        }
        mIfvExpand.setText(R.string.icon_down);
    }

}
