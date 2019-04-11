package com.grok.akm.movie.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.grok.akm.movie.Model.ApiResponse.ApiResponseMovie;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends ViewModel {

    private MutableLiveData<ApiResponseMovie> searchMovies;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Repository repository;

    public SearchViewModel(Repository repository){
        this.repository = repository;
        searchMovies = new MutableLiveData<>();
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

    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
