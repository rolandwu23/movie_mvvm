package com.grok.akm.movie.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.grok.akm.movie.Model.pojo.Movie;
import com.grok.akm.movie.Room.FavoriteRepository;

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

    public void unFavorite(String id){
        repository.unFavorite(id);
    }

    public boolean isFavorite(String id){
        return repository.isFavorite(id);
    }
}
