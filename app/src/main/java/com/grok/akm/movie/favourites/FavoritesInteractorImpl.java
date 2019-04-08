package com.grok.akm.movie.favourites;

import com.grok.akm.movie.pojo.Movie;

import java.util.List;

import javax.inject.Inject;

public class FavoritesInteractorImpl implements FavouritesInteractor
{
    private FavouritesStore favoritesStore;

    @Inject
    public FavoritesInteractorImpl(FavouritesStore store)
    {
        favoritesStore = store;
    }

    @Override
    public void setFavorite(Movie movie)
    {
        favoritesStore.setFavorite(movie);
    }

    @Override
    public boolean isFavorite(String id)
    {
        return favoritesStore.isFavorite(id);
    }

    @Override
    public List<Movie> getFavorites()
    {
        return favoritesStore.getFavorites();
    }

    @Override
    public void unFavorite(String id)
    {
        favoritesStore.unfavorite(id);
    }
}