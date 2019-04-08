package com.grok.akm.movie.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.grok.akm.movie.ApiResponseMovie;
import com.grok.akm.movie.ViewModel.Repository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NewestMovieViewModel extends ViewModel {

    private MutableLiveData<ApiResponseMovie> listLiveData;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Repository repository;

    public NewestMovieViewModel(Repository repository){
        this.repository = repository;
        listLiveData = new MutableLiveData<>();
    }

    public void getNewestMovies(){
        compositeDisposable.add(repository.newestMovie()
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
