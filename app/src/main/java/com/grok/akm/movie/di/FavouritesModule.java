package com.grok.akm.movie.di;

import com.grok.akm.movie.di.AppModule;
import com.grok.akm.movie.favourites.FavoritesInteractorImpl;
import com.grok.akm.movie.favourites.FavouritesInteractor;
import com.grok.akm.movie.favourites.FavouritesStore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = AppModule.class)
public class FavouritesModule {

    @Provides
    @Singleton
    FavouritesInteractor provideFavouritesInteractor(FavouritesStore store)
    {
        return new FavoritesInteractorImpl(store);
    }

}
