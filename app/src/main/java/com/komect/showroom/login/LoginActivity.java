package com.komect.showroom.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import com.komect.showroom.BaseActivity;
import com.komect.showroom.BuildConfig;
import com.komect.showroom.R;
import com.komect.showroom.ZtApp;
import com.komect.showroom.databinding.ActivityLoginBinding;
import com.komect.showroom.util.SimpleCacheUtil;
import javax.inject.Inject;

/**
 * A login screen.
 */
public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;

    @Inject
    LoginBean loginBean;
    @Inject
    LoginPresenter loginPresenter;
    @Inject
    SimpleCacheUtil cacheUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        getAppComponent().inject(this);
        ZtApp.init(this, BuildConfig.HTTP_HOST);

        initToolbar();

        loginBean.setPassword("123456");
        loginBean.setPhone("18000000000");
        binding.setLogin(loginBean);
        binding.setPresenter(loginPresenter);
        loginPresenter.bindView(this);
    }


    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }


    /**
     * 取消倒计时
     */
    public void onCancel() {
        timer.cancel();
        binding.txtGetcode.setText(R.string.getcode);
    }


    /**
     * 开始倒计时
     */
    public void onTimeStart() {
        timer.start();
    }


    /**
     * 倒计时
     */
    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            binding.txtGetcode.setText((millisUntilFinished / 1000) + getString(R.string.resent_after_as));
        }


        @Override
        public void onFinish() {
            binding.txtGetcode.setEnabled(true);
            binding.txtGetcode.setText(R.string.getcode);
        }
    };
}

