package com.grok.akm.movie.di;

import com.grok.akm.movie.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, UtilModule.class})
@Singleton
public interface AppComponent {

    void doInjection(MainActivity loginActivity);

}