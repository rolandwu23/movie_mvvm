package com.grok.akm.movie.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.grok.akm.movie.ApiResponseReview;
import com.grok.akm.movie.ViewModel.Repository;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ReviewsViewModel extends ViewModel {

    private MutableLiveData<ApiResponseReview> listLiveData;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Repository repository;

    public ReviewsViewModel(Repository repository){
        this.repository = repository;
        listLiveData = new MutableLiveData<>();
    }

    public void getReviews(String movieId){
        compositeDisposable.add(repository.getReviews(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> listLiveData.postValue(ApiResponseReview.loading()))
                .subscribe(
                        result -> listLiveData.postValue(ApiResponseReview.success(result)),
                        throwable -> listLiveData.postValue(ApiResponseReview.error(throwable))
                ));
    }

    public MutableLiveData<ApiResponseReview> getListLiveData(){return  listLiveData;}
    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
