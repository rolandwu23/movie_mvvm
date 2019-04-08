package com.grok.akm.movie;

import com.grok.akm.movie.Retrofit.Status;
import com.grok.akm.movie.pojo.MovieWrapper;

import io.reactivex.annotations.Nullable;

import static com.grok.akm.movie.Retrofit.Status.ERROR;
import static com.grok.akm.movie.Retrofit.Status.LOADING;
import static com.grok.akm.movie.Retrofit.Status.SUCCESS;

public class ApiResponseNewestMovie {

    public final Status status;

    @Nullable
    public final MovieWrapper data;

    @Nullable
    public final Throwable error;

    private ApiResponseNewestMovie(Status status, @Nullable MovieWrapper data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponseNewestMovie loading() {
        return new ApiResponseNewestMovie(LOADING, null, null);
    }

    public static ApiResponseNewestMovie success(@io.reactivex.annotations.NonNull MovieWrapper data) {
        return new ApiResponseNewestMovie(SUCCESS, data, null);
    }

    public static ApiResponseNewestMovie error(@io.reactivex.annotations.NonNull Throwable error) {
        return new ApiResponseNewestMovie(ERROR, null, error);
    }

}
