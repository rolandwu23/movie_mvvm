package com.grok.akm.movie.ViewModel;

import com.grok.akm.movie.Retrofit.ApiCallInterface;
import com.grok.akm.movie.pojo.MovieWrapper;

import io.reactivex.Observable;

public class Repository {

    private ApiCallInterface apiCallInterface;

    public Repository(ApiCallInterface apiCallInterface) {
        this.apiCallInterface = apiCallInterface;
    }

    /*
     * method to call login api
     * */

    public Observable<MovieWrapper> executeMovie(int page) {
        return apiCallInterface.popularMovies(page);
    }
}
