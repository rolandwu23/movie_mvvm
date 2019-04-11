package com.grok.akm.movie.di;

import android.app.Application;
import android.content.Context;



public class MyApplication extends Application {

    AppComponent appComponent;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this))
                .utilModule(new UtilModule())
                .sortOptionsModule(new SortOptionsModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

}
