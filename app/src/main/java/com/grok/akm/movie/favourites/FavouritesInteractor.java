package com.grok.akm.movie.favourites;

import com.grok.akm.movie.pojo.Movie;

import java.util.List;

public interface FavouritesInteractor {
    public void setFavorite(Movie movie);
    public boolean isFavorite(String id);
    public List<Movie> getFavorites();
    public void unFavorite(String id);
}
