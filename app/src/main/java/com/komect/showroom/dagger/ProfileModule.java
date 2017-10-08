package com.komect.showroom.dagger;

import com.komect.showroom.login.LoginBean;
import com.komect.showroom.login.LoginPresenter;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by lsl on 2017/8/7.
 */
@Module
public class ProfileModule {
    @Provides
    @Singleton
    LoginBean provideLoginBean() {
        return new LoginBean();
    }


    @Provides
    @Singleton
    LoginPresenter provideLoginPresenter() {
        return new LoginPresenter();
    }
}
