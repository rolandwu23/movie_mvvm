package com.grok.akm.movie.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.grok.akm.movie.Model.pojo.Movie;
import com.grok.akm.movie.Network.Status;
import com.grok.akm.movie.Paging.MovieDataSource;
import com.grok.akm.movie.Paging.MovieDataSourceFactory;

import io.reactivex.disposables.CompositeDisposable;

public class MainViewModel extends ViewModel {

    private MovieDataSourceFactory mostDataSourceFactory;
    private LiveData<PagedList<Movie>> mostMovies;
    private LiveData<Status> mostLoadStatus = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    public MainViewModel(Repository repository){
        mostDataSourceFactory = new MovieDataSourceFactory(repository,compositeDisposable);
        initializePaging();
    }

    private void initializePaging() {

        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(true)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(10).build();

        mostMovies = new LivePagedListBuilder<>(mostDataSourceFactory, pagedListConfig)
                .build();

        mostLoadStatus = Transformations.switchMap(mostDataSourceFactory.getLiveData(), MovieDataSource::getProgressLiveStatus);

    }

    public LiveData<Status> getMostLoadStatus() {
        return mostLoadStatus;
    }

    public LiveData<PagedList<Movie>> getMostMoviesLiveData() {
        return mostMovies;
    }

    public void refresh(){
        mostDataSourceFactory.getLiveData().getValue().invalidate();
    }


    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
