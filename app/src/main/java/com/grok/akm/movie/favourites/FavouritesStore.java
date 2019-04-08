package com.grok.akm.movie.favourites;

import com.grok.akm.movie.pojo.Movie;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmResults;

@Singleton
public class FavouritesStore {

    private Realm realm;

    @Inject
    public FavouritesStore(Realm realm)
    {
        this.realm = realm;
    }

    public void setFavorite(Movie movie)
    {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(new FavouriteRealmObject(movie));
        realm.commitTransaction();
    }

    public boolean isFavorite(String id)
    {
        FavouriteRealmObject res =  realm.where(FavouriteRealmObject.class).equalTo(FavouriteRealmObject.ID, id).findFirst();
        return res != null;
    }

    public List<Movie> getFavorites()
    {
        RealmResults<FavouriteRealmObject> res =  realm.where(FavouriteRealmObject.class).findAll();
        List<Movie> movies = new ArrayList<>();

        for(FavouriteRealmObject i : res){
            movies.add(movieRealmObjectToMovie(i));
        }

        return movies;
    }

    public void unfavorite(String id)
    {
        realm.beginTransaction();
        FavouriteRealmObject movie = realm.where(FavouriteRealmObject.class).equalTo(FavouriteRealmObject.ID, id).findFirst();
        if(movie != null)
            movie.deleteFromRealm();
        realm.commitTransaction();
    }

    private Movie movieRealmObjectToMovie(FavouriteRealmObject movieRealmObject){
        Movie movie = new Movie();
        movie.setId(movieRealmObject.getId());
        movie.setOverview(movieRealmObject.getOverview());
        movie.setReleaseDate(movieRealmObject.getReleaseDate());
        movie.setPosterPath(movieRealmObject.getPosterPath());
        movie.setBackdropPath(movieRealmObject.getBackdropPath());
        movie.setTitle(movieRealmObject.getTitle());
        movie.setVoteAverage(movieRealmObject.getVoteAverage());

        return movie;
    }
}
