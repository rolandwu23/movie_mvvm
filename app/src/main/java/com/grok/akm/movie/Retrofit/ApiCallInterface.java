package com.grok.akm.movie.Retrofit;

import com.grok.akm.movie.pojo.MovieWrapper;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCallInterface {


    @GET("3/discover/movie?language=en&sort_by=popularity.desc")
    Observable<MovieWrapper> popularMovies(@Query("page") int page);
}
