package com.grok.akm.movie.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.grok.akm.movie.Retrofit.Status;
import com.grok.akm.movie.paging.HighestMovieDataSource;
import com.grok.akm.movie.paging.HighestMovieDataSourceFactory;
import com.grok.akm.movie.paging.MovieDataSource;
import com.grok.akm.movie.paging.MovieDataSourceFactory;
import com.grok.akm.movie.pojo.Movie;

import io.reactivex.disposables.CompositeDisposable;

public class PagingHighestMovieViewModel extends ViewModel {

    private HighestMovieDataSourceFactory newsDataSourceFactory;
    private LiveData<PagedList<Movie>> listLiveData;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private LiveData<Status> progressLoadStatus = new MutableLiveData<>();

    public PagingHighestMovieViewModel(Repository repository) {
        newsDataSourceFactory = new HighestMovieDataSourceFactory(repository,compositeDisposable);
        initializePaging();
    }


    private void initializePaging() {

        PagedList.Config pagedListConfig =
                new PagedList.Config.Builder()
                        .setEnablePlaceholders(true)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(10).build();

        listLiveData = new LivePagedListBuilder<>(newsDataSourceFactory, pagedListConfig)
                .build();

        progressLoadStatus = Transformations.switchMap(newsDataSourceFactory.getLiveData(), HighestMovieDataSource::getProgressLiveStatus);

    }

    public LiveData<Status> getProgressLoadStatus() {
        return progressLoadStatus;
    }

    public LiveData<PagedList<Movie>> getListLiveData() {
        return listLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
