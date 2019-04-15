package com.grok.akm.movie.ViewModel;

import android.annotation.SuppressLint;

import com.grok.akm.movie.Model.pojo.MovieWrapper;
import com.grok.akm.movie.Model.pojo.ReviewWrapper;
import com.grok.akm.movie.Model.pojo.VideoWrapper;
import com.grok.akm.movie.Network.ApiCallInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.reactivex.Observable;

public class Repository {

    private ApiCallInterface apiCallInterface;
    @SuppressLint("SimpleDateFormat")
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final int NEWEST_MIN_VOTE_COUNT = 50;

    public Repository(ApiCallInterface apiCallInterface) {
        this.apiCallInterface = apiCallInterface;
    }

    /*
     * method to call login api
     * */

    public Observable<MovieWrapper> executeMovie(int page) {
        return apiCallInterface.popularMovies(page);
    }

    public Observable<MovieWrapper> searchMovie(String searchQuery) {
        return apiCallInterface.searchMovies(searchQuery);
    }

    public Observable<MovieWrapper> highestRatedMovie(int page){
        return apiCallInterface.highestRatedMovies(page);
    }

    public Observable<MovieWrapper> newestMovie(){
        Calendar cal = Calendar.getInstance();
        String maxReleaseDate = dateFormat.format(cal.getTime());
        return apiCallInterface.newestMovies(maxReleaseDate,NEWEST_MIN_VOTE_COUNT);
    }

    public Observable<VideoWrapper> getVideos(String movieId){
        return apiCallInterface.trailers(movieId);
    }

    public Observable<ReviewWrapper> getReviews(String movieId){
        return apiCallInterface.reviews(movieId);
    }
}
