package com.grok.akm.movie;

import android.app.Application;
import android.content.Context;

import com.grok.akm.movie.di.AppComponent;
import com.grok.akm.movie.di.AppModule;
import com.grok.akm.movie.di.DaggerAppComponent;
import com.grok.akm.movie.di.UtilModule;



public class MyApplication extends Application {

    AppComponent appComponent;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).utilModule(new UtilModule()).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

}
