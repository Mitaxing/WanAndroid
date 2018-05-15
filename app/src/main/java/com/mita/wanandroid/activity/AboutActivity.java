package com.mita.wanandroid.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mita.wanandroid.R;
import com.mita.wanandroid.adapter.AboutAdapter;
import com.mita.wanandroid.entity.About;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于页面
 *
 * @author Mita
 * @date 2018/5/10 16:30
 **/
public class AboutActivity extends BaseActivity {

    @BindView(R.id.rv_about)
    RecyclerView mRvAbout;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    private List<About> list = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_about);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mTvTitle.setText(R.string.main_menu_about);
        initData();
        initRv();
    }

    private void initData() {
        About about = new About(0, "网站内容");
        list.add(about);
        about = new About(1, "本网站每天新增20~30篇优质文章，并加入到现有分类中，力求整理出一份优质而又详尽的知识体系，闲暇时间不妨上来学习下知识； 除此以外，并为大家提供平时开发过程中常用的工具以及常用的网址导航。");
        list.add(about);
        about = new About(1, "当然这只是我们目前的功能，未来我们将提供更多更加便捷的功能...");
        list.add(about);
        about = new About(1, "如果您有任何好的建议:");
        list.add(about);
        about = new About(2, "—关于网站排版");
        list.add(about);
        about = new About(2, "—关于新增常用网址以及工具");
        list.add(about);
        about = new About(2, "—未来你希望增加的功能等");
        list.add(about);
        about = new About(1, "可以在 https://github.com/hongyangAndroid/xueandroid 项目中以issue的形式提出，我将及时跟进。");
        list.add(about);
        about = new About(1, "如果您希望长期关注本站，可以加入我们的QQ群：591683946");
        list.add(about);
        about = new About(0, "关于站长");
        list.add(about);
        about = new About(1, "鸿洋，长期在CSDN上编写高质量的博客:");
        list.add(about);
        about = new About(1, "blog.csdn.net/lmj623565791");
        list.add(about);
        about = new About(1, "并维护着一个微信公众号[hongyangAndroid]，欢迎关注一下表示对鸿神的支持:");
        list.add(about);
        about = new About(3, "");
        about.setImgRes(R.drawable.qr_code);
        list.add(about);
        about = new About(1, "每天早晨7点30分，为你推荐优秀技术博文，提升从上班路上的闲暇时间开始~");
        list.add(about);
    }

    private void initRv() {
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRvAbout.setLayoutManager(llm);
        AboutAdapter adapter = new AboutAdapter(this, list);
        mRvAbout.setAdapter(adapter);
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
}
