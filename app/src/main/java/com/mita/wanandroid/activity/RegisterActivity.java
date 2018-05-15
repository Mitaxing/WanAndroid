package com.mita.wanandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.mita.wanandroid.R;
import com.mita.wanandroid.http.request.RequestRegister;
import com.mita.wanandroid.http.result.ResultRegisterListener;
import com.mita.wanandroid.utils.IToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 注册页面
 *
 * @author Mita
 * @date 2018/4/9 15:38
 **/
public class RegisterActivity extends BaseActivity implements ResultRegisterListener {

    @BindView(R.id.atv_register_name)
    AutoCompleteTextView mEmailView;
    @BindView(R.id.atv_register_password)
    EditText mPasswordView;
    @BindView(R.id.atv_register_re_password)
    EditText mRePasswordView;
    @BindView(R.id.lav_wait)
    LottieAnimationView mProgressView;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_register)
    TextView mEmailSignInButton;

    private final int HANDLER_PROGRESS = 0;

    private String name, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mTvTitle.setText(R.string.register);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegister() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mRePasswordView.setError(null);

        // Store values at the time of the login attempt.
        name = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        String rePassword = mRePasswordView.getText().toString();

        // Check for a valid email address.
        if (TextUtils.isEmpty(name)) {
            mEmailView.setError(getString(R.string.error_empty));
            mEmailView.requestFocus();
            return;
        } else if (lengthEnough(name)) {
            mEmailView.setError(getString(R.string.error_short_email));
            mEmailView.requestFocus();
            return;
        }

        // Check for a valid rePassword, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_empty));
            mPasswordView.requestFocus();
            return;
        } else if (lengthEnough(password)) {
            mPasswordView.setError(getString(R.string.error_short_email));
            mPasswordView.requestFocus();
            return;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(rePassword)) {
            mRePasswordView.setError(getString(R.string.error_empty));
            mRePasswordView.requestFocus();
            return;
        } else if (!rePassword.equals(password)) {
            mRePasswordView.setError(getString(R.string.error_pwd_not_equal));
            mRePasswordView.requestFocus();
            return;
        }
        handler.sendEmptyMessageDelayed(HANDLER_PROGRESS,200);

        new RequestRegister(this,this).requestRegister(name, password);
        mEmailSignInButton.setClickable(false);
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
    public void registerSuccess() {
        Intent intent = new Intent();
        intent.putExtra("name", name);
        intent.putExtra("password", password);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void registerFail(String msg) {
        IToast.showToast(this, msg);
        handler.removeMessages(HANDLER_PROGRESS);
        mProgressView.setVisibility(View.GONE);
        mEmailSignInButton.setClickable(true);
    }

    @OnClick(R.id.btn_register)
    public void clickRegister(View v) {
        attemptRegister();
    }

    @OnClick(R.id.ifv_title_left)
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