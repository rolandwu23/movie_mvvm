package com.grok.akm.movie;

import com.grok.akm.movie.Retrofit.Status;
import com.grok.akm.movie.pojo.MovieWrapper;

import io.reactivex.annotations.Nullable;

import static com.grok.akm.movie.Retrofit.Status.ERROR;
import static com.grok.akm.movie.Retrofit.Status.LOADING;
import static com.grok.akm.movie.Retrofit.Status.SUCCESS;

public class ApiResponseMovie {


    public final Status status;

    @Nullable
    public final MovieWrapper data;

    @Nullable
    public final Throwable error;

    private ApiResponseMovie(Status status, @Nullable MovieWrapper data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponseMovie loading() {
        return new ApiResponseMovie(LOADING, null, null);
    }

    public static ApiResponseMovie success(@io.reactivex.annotations.NonNull MovieWrapper data) {
        return new ApiResponseMovie(SUCCESS, data, null);
    }

    public static ApiResponseMovie error(@io.reactivex.annotations.NonNull Throwable error) {
        return new ApiResponseMovie(ERROR, null, error);
    }



}