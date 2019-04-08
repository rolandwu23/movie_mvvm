package com.grok.akm.movie.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.view.View;

import com.grok.akm.movie.ApiResponseMovie;
import com.grok.akm.movie.Retrofit.Status;
import com.grok.akm.movie.SortType;
import com.grok.akm.movie.ViewModel.Repository;
import com.grok.akm.movie.paging.HighestMovieDataSource;
import com.grok.akm.movie.paging.HighestMovieDataSourceFactory;
import com.grok.akm.movie.paging.MovieDataSource;
import com.grok.akm.movie.paging.MovieDataSourceFactory;
import com.grok.akm.movie.pojo.Movie;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    private MutableLiveData<ApiResponseMovie> searchMovies;
    private MutableLiveData<ApiResponseMovie> newesetMovies;
    private MutableLiveData<SortType> statusLiveData;

    private HighestMovieDataSourceFactory highestDataSourceFactory;
    private LiveData<PagedList<Movie>> highestMovies;
    private LiveData<Status> highestLoadStatus = new MutableLiveData<>();

    private MovieDataSourceFactory mostDataSourceFactory;
    private LiveData<PagedList<Movie>> mostMovies;
    private LiveData<Status> mostLoadStatus = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Repository repository;

    public MainViewModel(Repository repository){
        this.repository = repository;
        searchMovies = new MutableLiveData<>();
        newesetMovies = new MutableLiveData<>();
        statusLiveData = new MutableLiveData<>();
        highestDataSourceFactory = new HighestMovieDataSourceFactory(repository,compositeDisposable);
        mostDataSourceFactory = new MovieDataSourceFactory(repository,compositeDisposable);
        initializeHighestPaging();
        initializeMostPaging();
    }

    public void getSearchMovies(String searchQuery){
        compositeDisposable.add(repository.searchMovie(searchQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> searchMovies.postValue(ApiResponseMovie.loading()))
                .subscribe(
                        result -> searchMovies.postValue(ApiResponseMovie.success(result)),
                        throwable -> searchMovies.postValue(ApiResponseMovie.error(throwable))
                ));
    }

    public MutableLiveData<ApiResponseMovie> getSearchMoviesLiveData(){return  searchMovies;}

    public void getNewestMovies(){
        compositeDisposable.add(repository.newestMovie()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> newesetMovies.postValue(ApiResponseMovie.loading()))
                .subscribe(
                        result -> newesetMovies.postValue(ApiResponseMovie.success(result)),
                        throwable -> newesetMovies.postValue(ApiResponseMovie.error(throwable))
                ));
    }

    public MutableLiveData<ApiResponseMovie> getNewesetMoviesLiveData(){return  newesetMovies;}

    public void setStatusLiveData(SortType status){
        statusLiveData.setValue(status);
    }

    public MutableLiveData<SortType> getStatusLiveData(){
        return statusLiveData;
    }

    private void initializeHighestPaging() {

        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(true)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(10).build();

        highestMovies = new LivePagedListBuilder<>(highestDataSourceFactory, pagedListConfig)
                .build();

        highestLoadStatus = Transformations.switchMap(highestDataSourceFactory.getLiveData(), HighestMovieDataSource::getProgressLiveStatus);

    }

    private void initializeMostPaging() {

        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(true)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(10).build();

        mostMovies = new LivePagedListBuilder<>(mostDataSourceFactory, pagedListConfig)
                .build();

        mostLoadStatus = Transformations.switchMap(mostDataSourceFactory.getLiveData(), MovieDataSource::getProgressLiveStatus);

    }


    public LiveData<Status> getHighestLoadStatus() {
        return highestLoadStatus;
    }

    public LiveData<PagedList<Movie>> getHighestMoviesLiveData() {
        return highestMovies;
    }

    public LiveData<Status> getMostLoadStatus() {
        return mostLoadStatus;
    }

    public LiveData<PagedList<Movie>> getMostMoviesLiveData() {
        return mostMovies;
    }


    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
