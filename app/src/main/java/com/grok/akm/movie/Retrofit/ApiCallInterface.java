package com.grok.akm.movie.Retrofit;

import com.grok.akm.movie.pojo.MovieWrapper;
import com.grok.akm.movie.pojo.ReviewWrapper;
import com.grok.akm.movie.pojo.VideoWrapper;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiCallInterface {


    @GET("3/discover/movie?language=en&sort_by=popularity.desc")
    Observable<MovieWrapper> popularMovies(@Query("page") int page);

    @GET("3/search/movie?language=en-US&page=1")
    Observable<MovieWrapper> searchMovies(@Query("query") String searchQuery);

    @GET("3/discover/movie?vote_count.gte=500&language=en&sort_by=vote_average.desc")
    Observable<MovieWrapper> highestRatedMovies(@Query("page") int page);

    @GET("3/discover/movie?language=en&sort_by=release_date.desc")
    Observable<MovieWrapper> newestMovies(@Query("release_date.lte") String maxReleaseDate,@Query("vote_count.gte") int minVoteCount);

    @GET("3/movie/{movieId}/videos")
    Observable<VideoWrapper> trailers(@Path("movieId") String movieId);

    @GET("3/movie/{movieId}/reviews")
    Observable<ReviewWrapper> reviews(@Path("movieId") String movieId);

}
