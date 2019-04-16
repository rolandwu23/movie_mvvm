package com.grok.akm.movie;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.grok.akm.movie.Model.pojo.Movie;
import com.grok.akm.movie.Paging.HighestMovieAdapter;
import com.grok.akm.movie.Utils.SortType;
import com.grok.akm.movie.ViewModel.FavoriteViewModel;
import com.grok.akm.movie.ViewModel.FragmentViewModel;
import com.grok.akm.movie.ViewModel.ViewModelFactory;
import com.grok.akm.movie.di.MyApplication;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoritesActivity extends AppCompatActivity{

    @Inject
    ViewModelFactory viewModelFactory;

    FavoriteViewModel favoriteViewModel;

    FragmentViewModel fragmentViewModel;

    HighestMovieAdapter adapter;

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerFrameLayout;

    @BindView(R.id.activity_main_recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        setToolbar();

        ((MyApplication) getApplication()).getAppComponent().doInjection(this);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);

        favoriteViewModel.getFavorites().observe(this,this::renderSuccessResponse);

        fragmentViewModel = ViewModelProviders.of(this,viewModelFactory).get(FragmentViewModel.class);

        fragmentViewModel.getStatusLiveData().observe(this, this::showSortOptions);
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.movie_guide);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    private void renderSuccessResponse(List<Movie> movies){
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        adapter = new HighestMovieAdapter(this,movies);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                displaySortOptions();
                break;
            case R.id.action_search:
                Intent intent = new Intent(FavoritesActivity.this,SearchActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySortOptions(){
        SortingDialogFragment sortingDialogFragment = SortingDialogFragment.newInstance();
        sortingDialogFragment.show(getSupportFragmentManager(), "Select Quantity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showSortOptions(SortType sortType){
        switch (sortType){
            case MOST_POPULAR:
                showMostPopularMovies();
                break;
            case HIGHEST_RATED:
                showHighestMovies();
                break;
            case NEWEST:
                showNewestMovies();
                break;
        }
    }

    private void showMostPopularMovies(){
        Intent intent = new Intent(this, MostActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void showHighestMovies(){
        Intent intent = new Intent(this,HighestActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void showNewestMovies(){
        Intent intent = new Intent(this,NewestActivity.class);
        startActivity(intent);
        this.finish();
    }

}
