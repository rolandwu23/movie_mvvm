package com.grok.akm.movie.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.grok.akm.movie.Model.pojo.Movie;
import com.grok.akm.movie.Network.Status;
import com.grok.akm.movie.Paging.HighestMovieDataSource;
import com.grok.akm.movie.Paging.HighestMovieDataSourceFactory;

import io.reactivex.disposables.CompositeDisposable;

public class HighestViewModel extends ViewModel {

    private HighestMovieDataSourceFactory highestDataSourceFactory;
    private LiveData<PagedList<Movie>> highestMovies;
    private LiveData<Status> highestLoadStatus = new MutableLiveData<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public HighestViewModel(Repository repository){
        highestDataSourceFactory = new HighestMovieDataSourceFactory(repository,compositeDisposable);
        initializePaging();
    }

    private void initializePaging() {

        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(true)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(10).build();

        highestMovies = new LivePagedListBuilder<>(highestDataSourceFactory, pagedListConfig)
                .build();

        highestLoadStatus = Transformations.switchMap(highestDataSourceFactory.getLiveData(), HighestMovieDataSource::getProgressLiveStatus);

    }

    public LiveData<Status> getHighestLoadStatus() {
        return highestLoadStatus;
    }

    public LiveData<PagedList<Movie>> getHighestMoviesLiveData() {
        return highestMovies;
    }


    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
