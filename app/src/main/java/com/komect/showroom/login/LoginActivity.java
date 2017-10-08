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
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        ZtApp.init(this, BuildConfig.HTTP_HOST);

        loginBean.setPassword("123456");
        loginBean.setPhone("18000000000");
        binding.setLogin(loginBean);
        binding.setPresenter(loginPresenter);
        //binding.toolbarTitle.setText(R.string.app_name);
        loginPresenter.bindView(this);
    }


    /**
     * 取消倒计时
     */
    public void onCancel() {
        timer.cancel();
        binding.txtGetcode.setText("获取验证码");
    }


    /**
     * 开始倒计时
     */
    public void onTimeStart() {
        timer.start();
    }


    private CountDownTimer timer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            binding.txtGetcode.setText((millisUntilFinished / 1000) + "秒后可重发");
        }


        @Override
        public void onFinish() {
            binding.txtGetcode.setEnabled(true);
            binding.txtGetcode.setText("获取验证码");
        }
    };
}

