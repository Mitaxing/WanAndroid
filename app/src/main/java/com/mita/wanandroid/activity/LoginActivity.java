package com.mita.wanandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.mita.wanandroid.R;
import com.mita.wanandroid.http.request.RequestLogin;
import com.mita.wanandroid.http.result.ResultLoginListener;
import com.mita.wanandroid.utils.IToast;
import com.mita.wanandroid.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录页面
 *
 * @author Mita
 * @date 2018/4/9 14:19
 **/
public class LoginActivity extends BaseActivity implements ResultLoginListener {

    @BindView(R.id.email)
    AutoCompleteTextView mEmailView;
    @BindView(R.id.password)
    EditText mPasswordView;
    @BindView(R.id.lav_wait)
    LottieAnimationView mProgressView;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.email_sign_in_button)
    TextView mEmailSignInButton;

    private final int HANDLER_PROGRESS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mTvTitle.setText(R.string.login);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String name = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        // Check for a valid email address.
        if (TextUtils.isEmpty(name)) {
            mEmailView.setError(getString(R.string.error_username_required));
            mEmailView.requestFocus();
            return;
        } else if (lengthEnough(name)) {
            mEmailView.setError(getString(R.string.error_short_email));
            mEmailView.requestFocus();
            return;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_empty));
            mPasswordView.requestFocus();
            return;
        } else if (lengthEnough(password)) {
            mPasswordView.setError(getString(R.string.error_short_email));
            mPasswordView.requestFocus();
            return;
        }

        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        requestLogin(name, password);
        mEmailSignInButton.setClickable(false);
    }

    /**
     * 请求登录
     *
     * @param name     用户名
     * @param password 密码
     */
    private void requestLogin(String name, String password) {
        handler.sendEmptyMessageDelayed(HANDLER_PROGRESS,200);
        new RequestLogin(this, this).requestLogin(name, password);
    }

    private boolean lengthEnough(String password) {
        return password.length() < 5;
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_PROGRESS:
                    mPasswordView.setVisibility(View.VISIBLE);
                    break;

                default:
                    break;
            }
            return false;
        }
    });

    @Override
    public void loginSuccess() {
        SpUtils.saveLoginInfo(this, mEmailView.getText().toString(), mPasswordView.getText().toString());
        finish();
    }

    @Override
    public void loginFail(String msg) {
        IToast.showToast(this, msg);
        handler.removeMessages(HANDLER_PROGRESS);
        mProgressView.setVisibility(View.GONE);
        mEmailSignInButton.setClickable(true);
    }

    @OnClick({R.id.ifv_title_left, R.id.email_sign_in_button, R.id.btn_register})
    public void clickMethod(View v) {
        switch (v.getId()) {
            case R.id.ifv_title_left:
                finish();
                break;

            case R.id.email_sign_in_button:
                attemptLogin();
                break;

            case R.id.btn_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivityForResult(intent, 0);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            String password = data.getStringExtra("password");
            mEmailView.setText(name);
            mPasswordView.setText(password);
            requestLogin(name, password);
        }
    }
}