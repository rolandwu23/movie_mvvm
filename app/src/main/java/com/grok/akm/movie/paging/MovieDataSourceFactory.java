package com.grok.akm.movie.paging;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.grok.akm.movie.ViewModel.Repository;
import com.grok.akm.movie.pojo.Movie;

import io.reactivex.disposables.CompositeDisposable;

public class MovieDataSourceFactory extends DataSource.Factory<Integer, Movie>  {

    private MutableLiveData<MovieDataSource> liveData;
    private CompositeDisposable disposable;
    private Repository repository;


    public MovieDataSourceFactory(Repository repository,CompositeDisposable disposable){
        this.repository = repository;
        this.disposable = disposable;
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<MovieDataSource> getLiveData() {return liveData;}


    @Override
    public DataSource<Integer, Movie> create() {
        MovieDataSource source = new MovieDataSource(repository,disposable);
        liveData.postValue(source);
        return source;
    }
}
