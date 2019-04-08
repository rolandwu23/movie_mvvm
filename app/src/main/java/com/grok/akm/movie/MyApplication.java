package com.grok.akm.movie;

import android.app.Application;
import android.content.Context;

import com.grok.akm.movie.di.AppComponent;
import com.grok.akm.movie.di.AppModule;
import com.grok.akm.movie.di.DaggerAppComponent;
import com.grok.akm.movie.di.SortOptionsModule;
import com.grok.akm.movie.di.UtilModule;
import com.grok.akm.movie.di.FavouritesModule;

import io.realm.Realm;


public class MyApplication extends Application {

    AppComponent appComponent;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
        context = this;
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this))
                .utilModule(new UtilModule())
                .sortOptionsModule(new SortOptionsModule(this))
                .favouritesModule(new FavouritesModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private void initRealm(){
        Realm.init(this);
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

}
