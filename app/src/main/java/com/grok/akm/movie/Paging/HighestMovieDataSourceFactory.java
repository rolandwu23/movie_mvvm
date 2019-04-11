package com.grok.akm.movie.Paging;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.grok.akm.movie.ViewModel.Repository;
import com.grok.akm.movie.Model.pojo.Movie;

import io.reactivex.disposables.CompositeDisposable;

public class HighestMovieDataSourceFactory extends DataSource.Factory<Integer, Movie> {

    private MutableLiveData<HighestMovieDataSource> highestLiveData;
    private CompositeDisposable disposable;
    private Repository repository;


    public HighestMovieDataSourceFactory(Repository repository,CompositeDisposable disposable){
        this.repository = repository;
        this.disposable = disposable;
        highestLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<HighestMovieDataSource> getLiveData() {
        return highestLiveData;
    }


    @Override
    public DataSource<Integer, Movie> create() {
        HighestMovieDataSource source = new HighestMovieDataSource(repository, disposable);
        highestLiveData.postValue(source);
        return source;
    }

}
