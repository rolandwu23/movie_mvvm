package com.grok.akm.movie;

import com.grok.akm.movie.Retrofit.Status;
import com.grok.akm.movie.pojo.MovieWrapper;
import com.grok.akm.movie.pojo.VideoWrapper;

import io.reactivex.annotations.Nullable;

import static com.grok.akm.movie.Retrofit.Status.ERROR;
import static com.grok.akm.movie.Retrofit.Status.LOADING;
import static com.grok.akm.movie.Retrofit.Status.SUCCESS;

public class ApiResponseTrailer {

    public final Status status;

    @Nullable
    public final VideoWrapper data;

    @Nullable
    public final Throwable error;

    private ApiResponseTrailer(Status status, @Nullable VideoWrapper data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponseTrailer loading() {
        return new ApiResponseTrailer(LOADING, null, null);
    }

    public static ApiResponseTrailer success(@io.reactivex.annotations.NonNull VideoWrapper data) {
        return new ApiResponseTrailer(SUCCESS, data, null);
    }

    public static ApiResponseTrailer error(@io.reactivex.annotations.NonNull Throwable error) {
        return new ApiResponseTrailer(ERROR, null, error);
    }

}
