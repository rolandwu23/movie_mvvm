package com.grok.akm.movie.Paging;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.grok.akm.movie.Model.pojo.Movie;
import com.grok.akm.movie.ViewModel.Repository;

import io.reactivex.disposables.CompositeDisposable;

public class MovieDataSourceFactory extends DataSource.Factory<Integer, Movie>  {

    private MutableLiveData<MovieDataSource> mostpopularLiveData;
    private CompositeDisposable disposable;
    private Repository repository;


    public MovieDataSourceFactory(Repository repository,CompositeDisposable disposable){
        this.repository = repository;
        this.disposable = disposable;
        mostpopularLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<MovieDataSource> getLiveData() {
       return mostpopularLiveData;
    }


    @Override
    public DataSource<Integer, Movie> create() {


        MovieDataSource source = new MovieDataSource(repository, disposable);
        mostpopularLiveData.postValue(source);
        return source;
    }
}
