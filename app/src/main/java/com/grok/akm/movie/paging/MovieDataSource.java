package com.grok.akm.movie.paging;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.grok.akm.movie.ViewModel.Repository;
import com.grok.akm.movie.Retrofit.Status;
import com.grok.akm.movie.pojo.Movie;


import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {

    private Repository repository;
    private final CompositeDisposable disposables;
    private MutableLiveData<Status> progressLiveStatus;

    MovieDataSource(Repository repository, CompositeDisposable disposables){
        this.repository = repository;
        this.disposables = disposables;
        progressLiveStatus = new MutableLiveData<>();
    }

    public MutableLiveData<Status> getProgressLiveStatus() {
        return progressLiveStatus;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Movie> callback) {

        disposables.add(repository.executeMovie(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> progressLiveStatus.postValue(Status.INITIAL))
                .subscribe(
                        result -> {
                                    progressLiveStatus.postValue(Status.SUCCESS);
                                    callback.onResult(result.getMovieList(), null, 1);

                        },
                        throwable -> progressLiveStatus.postValue(Status.ERROR))
                );
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {


        disposables.add(repository.executeMovie(params.key)
          .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> progressLiveStatus.postValue(Status.LOADING))
                .subscribe(
                        result -> {
                                int page = result.getTotal_pages();
                                progressLiveStatus.postValue(Status.SUCCESS);
                                callback.onResult(result.getMovieList(), params.key == page ? null : params.key + 1);
                        },
                        throwable -> progressLiveStatus.postValue(Status.ERROR)
                ));
    }
}
