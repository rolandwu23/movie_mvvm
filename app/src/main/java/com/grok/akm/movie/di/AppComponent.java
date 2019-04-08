package com.grok.akm.movie.di;

import com.grok.akm.movie.DetailsActivity;
import com.grok.akm.movie.MainActivity;
import com.grok.akm.movie.SortingDialogFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, UtilModule.class,SortOptionsModule.class,FavouritesModule.class})
@Singleton
public interface AppComponent {

    void doInjection(MainActivity loginActivity);

    void doInjection(SortingDialogFragment fragment);

    void doInjection(DetailsActivity detailsActivity);

}