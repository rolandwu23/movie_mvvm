package com.grok.akm.movie.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.grok.akm.movie.Model.ApiResponse.ApiResponseMovie;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NewestViewModel extends ViewModel {

    private MutableLiveData<ApiResponseMovie> newesetMovies;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Repository repository;

    public NewestViewModel(Repository repository){
        this.repository = repository;
        newesetMovies = new MutableLiveData<>();
    }

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


    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
