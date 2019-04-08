package com.grok.akm.movie.di;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.grok.akm.movie.SortType;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class SortOptionsModule {

    private Context context;

    public SortOptionsModule(Context context){this.context = context;}

    @Provides
    @Singleton
    SortPreferences getSortPref(){return new SortPreferences(context);}

}
