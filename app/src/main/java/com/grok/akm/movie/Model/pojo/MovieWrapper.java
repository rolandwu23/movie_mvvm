package com.grok.akm.movie.Model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieWrapper {

    @SerializedName("results")
    @Expose
    private List<Movie> movies;

    @SerializedName("total_pages")
    private int total_pages;

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<Movie> getMovieList() {
        return movies;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movies = movieList;
    }

}
