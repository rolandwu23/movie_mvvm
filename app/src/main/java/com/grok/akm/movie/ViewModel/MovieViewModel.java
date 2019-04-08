package com.grok.akm.movie.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.grok.akm.movie.ApiResponseMovie;
import com.grok.akm.movie.Retrofit.Status;
import com.grok.akm.movie.ViewModel.Repository;
import com.grok.akm.movie.pojo.Movie;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MovieViewModel extends ViewModel {

    private MutableLiveData<ApiResponseMovie> listLiveData;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Repository repository;

    public MovieViewModel(Repository repository){
        this.repository = repository;
        listLiveData = new MutableLiveData<>();
    }

    public void getSearchMovies(String searchQuery){
        compositeDisposable.add(repository.searchMovie(searchQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> listLiveData.postValue(ApiResponseMovie.loading()))
                .subscribe(
                        result -> listLiveData.postValue(ApiResponseMovie.success(result)),
                        throwable -> listLiveData.postValue(ApiResponseMovie.error(throwable))
                ));
    }

    public MutableLiveData<ApiResponseMovie> getListLiveData(){return  listLiveData;}
    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
