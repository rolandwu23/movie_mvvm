package com.grok.akm.movie;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.grok.akm.movie.ViewModel.ReviewsViewModel;
import com.grok.akm.movie.ViewModel.TrailersViewModel;
import com.grok.akm.movie.ViewModel.ViewModelFactory;
import com.grok.akm.movie.favourites.FavouritesInteractor;
import com.grok.akm.movie.pojo.Movie;
import com.grok.akm.movie.pojo.Review;
import com.grok.akm.movie.pojo.Video;

import java.util.List;

import javax.inject.Inject;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    // View name of the header image. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_IMAGE = "detail:header:image";

    // View name of the header title. Used for activity scene transitions
    public static final String VIEW_NAME_HEADER_TITLE = "detail:header:title";

    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    FavouritesInteractor favouritesInteractor;

    ImageView poster;
    CollapsingToolbarLayout collapsingToolbar;
    TextView title;
    TextView releaseDate;
    TextView rating;
    TextView overview;
    TextView label;
    LinearLayout trailers;
    HorizontalScrollView horizontalScrollView;
    TextView reviews;
    LinearLayout reviewsContainer;
    FloatingActionButton favorite;
    @Nullable
    Toolbar toolbar;

    private Movie movie;

    private TrailersViewModel trailersViewModel;

    private ReviewsViewModel reviewsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        movie = getIntent().getParcelableExtra(Constant.MOVIE);

        ((MyApplication) getApplication()).getAppComponent().doInjection(this);

        poster = (ImageView) findViewById(R.id.movie_poster);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        title = (TextView) findViewById(R.id.movie_name);
        releaseDate = (TextView) findViewById(R.id.movie_year);
        rating = (TextView) findViewById(R.id.movie_rating);
        overview = (TextView) findViewById(R.id.movie_description);
        label = (TextView) findViewById(R.id.trailers_label);
        trailers = (LinearLayout) findViewById(R.id.trailers);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.trailers_container);
        reviews = (TextView) findViewById(R.id.reviews_label);
        reviewsContainer = (LinearLayout) findViewById(R.id.reviews);
        favorite = (FloatingActionButton) findViewById(R.id.favorite);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        favorite.setOnClickListener(v -> {onFavouriteClick();});

        ViewCompat.setTransitionName(poster,VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(title,VIEW_NAME_HEADER_TITLE);

        trailersViewModel = ViewModelProviders.of(this,viewModelFactory).get(TrailersViewModel.class);

        trailersViewModel.getTrailers(movie.getId());

        trailersViewModel.getListLiveData().observe(this, this::consumeTrailerResponse);

        reviewsViewModel = ViewModelProviders.of(this,viewModelFactory).get(ReviewsViewModel.class);

        reviewsViewModel.getReviews(movie.getId());

        reviewsViewModel.getListLiveData().observe(this, this::consumeReviewResponse);

        setToolbar();

        showDetails(movie);

        showFavourite();
    }

    private void setToolbar()
    {
        collapsingToolbar.setContentScrimColor(ContextCompat.getColor(this, R.color.colorPrimary));
        collapsingToolbar.setTitle(getString(R.string.movie_details));
        collapsingToolbar.setCollapsedTitleTextAppearance(R.style.CollapsedToolbar);
        collapsingToolbar.setExpandedTitleTextAppearance(R.style.ExpandedToolbar);
        collapsingToolbar.setTitleEnabled(true);

        if (toolbar != null)
        {
            ((AppCompatActivity) this).setSupportActionBar(toolbar);

            ActionBar actionBar = ((AppCompatActivity) this).getSupportActionBar();
            if (actionBar != null)
            {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        } else
        {
            // Don't inflate. Tablet is in landscape mode.
        }
    }

    private void showDetails(Movie movie){
        Glide.with(this)
                .load(Constant.getBackdropPath(movie.getBackdropPath()))
                .into(poster);
        title.setText(movie.getTitle());
        releaseDate.setText(String.format(getString(R.string.release_date), movie.getReleaseDate()));
        rating.setText(String.format(getString(R.string.rating), String.valueOf(movie.getVoteAverage())));
        overview.setText(movie.getOverview());
    }

    private void consumeTrailerResponse(ApiResponseTrailer apiResponseTrailer){
        switch (apiResponseTrailer.status) {

            case LOADING:
                Snackbar.make(findViewById(android.R.id.content), "Loading Trailers...", Snackbar.LENGTH_SHORT)
                        .show();
                break;

            case SUCCESS:
                showTrailers(apiResponseTrailer.data.getVideos());
                break;

            case ERROR:
                Toast.makeText(DetailsActivity.this,getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    private void consumeReviewResponse(ApiResponseReview apiResponseReview){
        switch (apiResponseReview.status) {

            case LOADING:
                Snackbar.make(findViewById(android.R.id.content), "Loading Reviews...", Snackbar.LENGTH_SHORT)
                        .show();
                break;

            case SUCCESS:
                showReviews(apiResponseReview.data.getReviews());
                break;

            case ERROR:
                Toast.makeText(DetailsActivity.this,getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    private void showTrailers(List<Video> videos){

        if (videos.isEmpty())
        {
            label.setVisibility(View.GONE);
            this.trailers.setVisibility(View.GONE);
            horizontalScrollView.setVisibility(View.GONE);

        } else
        {
            label.setVisibility(View.VISIBLE);
            this.trailers.setVisibility(View.VISIBLE);
            horizontalScrollView.setVisibility(View.VISIBLE);

            this.trailers.removeAllViews();
            LayoutInflater inflater = this.getLayoutInflater();
            RequestOptions options = new RequestOptions()
                    .placeholder(R.color.colorPrimary)
                    .centerCrop()
                    .override(150, 150);

            for (Video trailer : videos)
            {
                View thumbContainer = inflater.inflate(R.layout.video, this.trailers, false);
                ImageView thumbView = thumbContainer.findViewById(R.id.video_thumb);
                thumbView.setTag(R.id.glide_tag, Video.getUrl(trailer));
                thumbView.requestLayout();
                thumbView.setOnClickListener(this);
                Glide.with(this)
                        .load(Video.getThumbnailUrl(trailer))
                        .apply(options)
                        .into(thumbView);
                this.trailers.addView(thumbContainer);
            }
        }
    }

    private void showReviews(List<Review> reviews){

        if (reviews.isEmpty())
        {
            this.reviews.setVisibility(View.GONE);
            reviewsContainer.setVisibility(View.GONE);
        } else
        {
            this.reviews.setVisibility(View.VISIBLE);
            reviewsContainer.setVisibility(View.VISIBLE);

            reviewsContainer.removeAllViews();
            LayoutInflater inflater = this.getLayoutInflater();
            for (Review review : reviews)
            {
                ViewGroup reviewContainer = (ViewGroup) inflater.inflate(R.layout.review, reviewsContainer, false);
                TextView reviewAuthor = reviewContainer.findViewById(R.id.review_author);
                TextView reviewContent = reviewContainer.findViewById(R.id.review_content);
                reviewAuthor.setText(review.getAuthor());
                reviewContent.setText(review.getContent());
                reviewContent.setOnClickListener(this);
                reviewsContainer.addView(reviewContainer);
            }
        }
    }

    private void showFavourite(){
        if(favouritesInteractor.isFavorite(movie.getId())){
            favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp));
        }else{
            favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
        }
    }

    private void onReviewClick(TextView view)
    {
        if (view.getMaxLines() == 5)
        {
            view.setMaxLines(500);
        } else
        {
            view.setMaxLines(5);
        }
    }

    private void onThumbnailClick(View view)
    {
        String videoUrl = (String) view.getTag(R.id.glide_tag);
        Intent playVideoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
        startActivity(playVideoIntent);
    }

    private void onFavouriteClick(){
        boolean favourite = favouritesInteractor.isFavorite(movie.getId());
                if(favourite){
                    favouritesInteractor.unFavorite(movie.getId());
                    favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
                }else{
                    favouritesInteractor.setFavorite(movie);
                    favorite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp));
                }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.video_thumb:
                onThumbnailClick(view);
                break;

            case R.id.review_content:
                onReviewClick((TextView) view);
                break;

            default:
                break;
        }
    }


}