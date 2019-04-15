package com.grok.akm.movie.di;


import com.grok.akm.movie.DetailsActivity;
import com.grok.akm.movie.FavoritesActivity;
import com.grok.akm.movie.HighestActivity;
import com.grok.akm.movie.MainActivity;
import com.grok.akm.movie.MostActivity;
import com.grok.akm.movie.NewestActivity;
import com.grok.akm.movie.SearchActivity;
import com.grok.akm.movie.SortingDialogFragment;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {AppModule.class, UtilModule.class,SortOptionsModule.class})
@Singleton
public interface AppComponent {

    void doInjection(MainActivity mainActivity);

    void doInjection(MostActivity loginActivity);

    void doInjection(SortingDialogFragment fragment);

    void doInjection(DetailsActivity detailsActivity);

    void doInjection(FavoritesActivity favoritesActivity);

    void doInjection(NewestActivity newestActivity);

    void doInjection(HighestActivity highestActivity);

    void doInjection(SearchActivity searchActivity);

}