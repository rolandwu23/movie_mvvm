package com.grok.akm.movie;

import com.grok.akm.movie.Retrofit.Status;
import com.grok.akm.movie.pojo.ReviewWrapper;
import com.grok.akm.movie.pojo.VideoWrapper;

import io.reactivex.annotations.Nullable;

import static com.grok.akm.movie.Retrofit.Status.ERROR;
import static com.grok.akm.movie.Retrofit.Status.LOADING;
import static com.grok.akm.movie.Retrofit.Status.SUCCESS;

public class ApiResponseReview {

    public final Status status;

    @Nullable
    public final ReviewWrapper data;

    @Nullable
    public final Throwable error;

    private ApiResponseReview(Status status, @Nullable ReviewWrapper data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponseReview loading() {
        return new ApiResponseReview(LOADING, null, null);
    }

    public static ApiResponseReview success(@io.reactivex.annotations.NonNull ReviewWrapper data) {
        return new ApiResponseReview(SUCCESS, data, null);
    }

    public static ApiResponseReview error(@io.reactivex.annotations.NonNull Throwable error) {
        return new ApiResponseReview(ERROR, null, error);
    }

}
