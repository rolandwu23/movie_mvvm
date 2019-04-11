package com.grok.akm.movie.Paging;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.grok.akm.movie.Network.Status;
import com.grok.akm.movie.ViewModel.Repository;
import com.grok.akm.movie.Model.pojo.Movie;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HighestMovieDataSource extends PageKeyedDataSource<Integer,Movie>{

    private Repository repository;
    private final CompositeDisposable disposables;
    private MutableLiveData<Status> progressLiveStatus;

    HighestMovieDataSource(Repository repository, CompositeDisposable disposables){
        this.repository = repository;
        this.disposables = disposables;
        progressLiveStatus = new MutableLiveData<>();
    }

    public MutableLiveData<Status> getProgressLiveStatus() {
        return progressLiveStatus;
    }

    @Override
    public void loadInitial(@NonNull PageKeyedDataSource.LoadInitialParams<Integer> params, @NonNull PageKeyedDataSource.LoadInitialCallback<Integer, Movie> callback) {

        disposables.add(repository.highestRatedMovie(1)
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
    public void loadBefore(@NonNull PageKeyedDataSource.LoadParams<Integer> params, @NonNull PageKeyedDataSource.LoadCallback<Integer, Movie> callback) {

    }

    @Override
    public void loadAfter(@NonNull PageKeyedDataSource.LoadParams<Integer> params, @NonNull PageKeyedDataSource.LoadCallback<Integer, Movie> callback) {


        disposables.add(repository.highestRatedMovie(params.key + 1)
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
