package com.komect.showroom.dagger;

import android.content.Context;
import com.komect.showroom.util.SimpleCacheUtil;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * Created by lsl on 2017/8/7.
 */
@Module
public class AppModule {
    private Context mContext;


    public AppModule(Context context) {
        mContext = context;
    }

    @Provides
    @Singleton
    SimpleCacheUtil provideSimpleCacheUtil() {
        return SimpleCacheUtil.get(mContext);
    }

}
