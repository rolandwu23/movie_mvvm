package com.grok.akm.movie.Room;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.grok.akm.movie.Model.pojo.Movie;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {

    private FavoriteRepository repository;

    public FavoriteViewModel(Application application){
        super(application);
        repository = new FavoriteRepository(application);
    }

    public LiveData<List<Movie>> getFavorites() {
        return repository.getFavorites();
    }

    public void setFavorite(Movie movie){
        repository.setFavorite(movie);
    }

    public void unFavorite(Movie movie){
        repository.unFavorite(movie);
    }

    public boolean isFavorite(Movie movie){
        return repository.isFavorite(movie);
    }
}
