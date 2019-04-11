package com.grok.akm.movie.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.grok.akm.movie.Model.ApiResponse.ApiResponseReview;
import com.grok.akm.movie.Model.ApiResponse.ApiResponseTrailer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DetailsViewModel extends ViewModel {

    private MutableLiveData<ApiResponseTrailer> trailers;
    private MutableLiveData<ApiResponseReview> reviews;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Repository repository;

    public DetailsViewModel(Repository repository){
        this.repository = repository;
        trailers = new MutableLiveData<>();
        reviews = new MutableLiveData<>();
    }

    public void getTrailers(String movieId){
        compositeDisposable.add(repository.getVideos(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> trailers.postValue(ApiResponseTrailer.loading()))
                .subscribe(
                        result -> trailers.postValue(ApiResponseTrailer.success(result)),
                        throwable -> trailers.postValue(ApiResponseTrailer.error(throwable))
                ));
    }

    public MutableLiveData<ApiResponseTrailer> getTrailersLiveData(){return  trailers;}

    public void getReviews(String movieId){
        compositeDisposable.add(repository.getReviews(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> reviews.postValue(ApiResponseReview.loading()))
                .subscribe(
                        result -> reviews.postValue(ApiResponseReview.success(result)),
                        throwable -> reviews.postValue(ApiResponseReview.error(throwable))
                ));
    }

    public MutableLiveData<ApiResponseReview> getReviewsLiveData(){return  reviews;}
    @Override
    protected void onCleared() {
        compositeDisposable.clear();
        super.onCleared();
    }
}
