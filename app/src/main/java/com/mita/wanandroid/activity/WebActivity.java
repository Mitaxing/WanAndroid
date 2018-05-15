package com.mita.wanandroid.activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mita.wanandroid.R;
import com.mita.wanandroid.dao.option.CollectionOption;
import com.mita.wanandroid.http.request.RequestCollectIn;
import com.mita.wanandroid.http.request.RequestUncollect;
import com.mita.wanandroid.http.result.ResultLoginListener;
import com.mita.wanandroid.onekeyshare.OnekeyShare;
import com.mita.wanandroid.utils.Constant;
import com.mita.wanandroid.utils.SpUtils;
import com.mita.wanandroid.view.IconFontView;
import com.mita.wanandroid.view.X5WebView;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 网页
 *
 * @author Mita
 * @date 2018/3/28 17:00
 **/
public class WebActivity extends BaseActivity implements ResultLoginListener {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ifv_title_right)
    IconFontView mIfvShare;
    @BindView(R.id.web_view)
    X5WebView mWebView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.web_progress)
    ProgressBar mProgressBar;

    private int articleId;
    private String articleUrl, articleTitle;
    private boolean isCollect = true, canCollect = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_web);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mIfvShare.setVisibility(View.VISIBLE);
        mIfvShare.setText(R.string.icon_share);

        articleUrl = getIntent().getStringExtra("url");
        articleId = getIntent().getIntExtra("articleId", -1);
        int type = getIntent().getIntExtra("type", -1);

        mWebView.loadUrl(articleUrl);

        //设置WebChromeClient类
        mWebView.setWebChromeClient(new WebChromeClient() {

            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                mTvTitle.setText(title);
                articleTitle = title;
            }

            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });

        if (type == Constant.NO_COLLOECT) {
            fab.setVisibility(View.GONE);
        } else {
            isCollect = !CollectionOption.hasCollected(articleId);
            changeCollect();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(articleTitle);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(articleUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(articleTitle);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(articleUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("值得一看！");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(articleTitle);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(articleUrl);

        // 启动分享GUI
        oks.show(this);
    }

    @OnClick({R.id.ifv_title_left, R.id.ifv_title_right, R.id.fab})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ifv_title_right:
                showShare();
                break;

            case R.id.ifv_title_left:
                finish();
                break;

            case R.id.fab:
                clickCollect();
                break;

            default:
                break;
        }
    }

    /**
     * 收藏操作
     */
    private void clickCollect() {
        String name = SpUtils.getLoginName(this);
        Log.i("xing", "WebActivity--Login Name：" + name);

        //还没登录
        if (TextUtils.isEmpty(name)) {
            Snackbar.make(fab, R.string.please_login, Snackbar.LENGTH_LONG)
                    .setAction(R.string.login, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startIntent(LoginActivity.class);
                        }
                    }).show();
        } else {
            if (canCollect) {
                canCollect = false;
                if (isCollect) {
                    new RequestUncollect(this, this).requestUnCollect(articleId);
                } else {
                    new RequestCollectIn(this, this).requestCollectIn(articleId);
                }
            }
        }
    }

    @Override
    public void loginSuccess() {
        changeCollect();
        int msgId = isCollect ? R.string.collect_success : R.string.uncollect_success;
        Snackbar.make(fab, msgId, Snackbar.LENGTH_SHORT).show();
        if (isCollect) {
            CollectionOption.saveCollectionId(articleId);
        } else {
            CollectionOption.deleteCollectionId(articleId);
        }
        canCollect = true;
    }

    @Override
    public void loginFail(String msg) {
        String message = isCollect ? "收藏失败，" : "取消收藏失败，";
        Snackbar.make(fab, message + msg, Snackbar.LENGTH_SHORT).show();
        canCollect = true;
    }

    /**
     * 改变收藏状态
     */
    private void changeCollect() {
        isCollect = !isCollect;
        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(getResources(), R.drawable.ic_collect, getTheme());
        if (vectorDrawableCompat == null) {
            return;
        }
        vectorDrawableCompat.setTint(ContextCompat.getColor(this, isCollect ? R.color.colorAccent : R.color.white));
        fab.setImageDrawable(vectorDrawableCompat);
        fab.setBackgroundTintList(ColorStateList.valueOf(isCollect ? Color.WHITE : ContextCompat.getColor(this, R.color.colorAccent)));
    }
}