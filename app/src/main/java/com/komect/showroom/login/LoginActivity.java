package com.komect.showroom.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.komect.showroom.BaseActivity;
import com.komect.showroom.MainActivity;
import com.komect.showroom.R;
import com.komect.showroom.ZtApp;
import com.komect.showroom.databinding.ActivityLoginBinding;
import com.komect.showroom.util.SimpleCacheUtil;

/**
 * A login screen.
 */
public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;

    LoginBean loginBean;
    LoginPresenter loginPresenter;
    SimpleCacheUtil cacheUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        ZtApp.init(this, "http://172.23.23.54:8080");

        loginBean = new LoginBean();
        loginBean.setPassword("123456");
        loginBean.setPhone("18800000000");
        binding.setLogin(loginBean);

        loginPresenter = new LoginPresenter();
        binding.setPresenter(loginPresenter);
    }


    public void loginResult() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

