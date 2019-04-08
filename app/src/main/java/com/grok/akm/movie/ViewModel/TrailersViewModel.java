package com.grok.akm.movie.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.grok.akm.movie.ApiResponseTrailer;
import com.grok.akm.movie.ViewModel.Repository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TrailersViewModel extends ViewModel {

    private MutableLiveData<ApiResponseTrailer> listLiveData;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Repository repository;

    public TrailersViewModel(Repository repository){
        this.repository = repository;
        listLiveData = new MutableLiveData<>();
    }

    public void getTrailers(String movieId){
        compositeDisposable.add(repository.getVideos(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> listLiveData.postValue(ApiResponseTrailer.loading()))
                .subscribe(
                        result -> listLiveData.postValue(ApiResponseTrailer.success(result)),
                        throwable -> listLiveData.postValue(ApiResponseTrailer.error(throwable))
                ));
    }

    public MutableLiveData<ApiResponseTrailer> getListLiveData(){return  listLiveData;}
    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
