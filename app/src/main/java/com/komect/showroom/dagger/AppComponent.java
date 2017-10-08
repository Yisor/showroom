package com.komect.showroom.dagger;

import android.content.Context;
import com.komect.showroom.login.LoginActivity;
import dagger.Component;
import javax.inject.Singleton;

/**
 * Created by lsl on 2017/8/7.
 */
@Singleton
@Component(modules = { AppModule.class, ProfileModule.class })
public abstract class AppComponent {

    private static AppComponent sComponent;


    public static AppComponent getInstance(Context context) {
        if (sComponent == null) {
            sComponent = DaggerAppComponent.builder()
                                           .appModule(new AppModule(context))
                                           .profileModule(new ProfileModule())
                                           .build();
        }
        return sComponent;
    }


    /**
     * @param activity
     */
    public abstract void inject(LoginActivity activity);
}
