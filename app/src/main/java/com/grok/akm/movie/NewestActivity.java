package com.grok.akm.movie;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.grok.akm.movie.Model.ApiResponse.ApiResponseMovie;
import com.grok.akm.movie.Model.pojo.Movie;
import com.grok.akm.movie.Paging.HighestMovieAdapter;
import com.grok.akm.movie.Utils.RxUtils;
import com.grok.akm.movie.Utils.SortType;
import com.grok.akm.movie.ViewModel.FragmentViewModel;
import com.grok.akm.movie.ViewModel.NewestViewModel;
import com.grok.akm.movie.ViewModel.SearchViewModel;
import com.grok.akm.movie.ViewModel.ViewModelFactory;
import com.grok.akm.movie.di.MyApplication;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class NewestActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    NewestViewModel newestViewModel;

    SearchViewModel searchViewModel;

    FragmentViewModel fragmentViewModel;
    HighestMovieAdapter adapter;

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerFrameLayout;

    @BindView(R.id.activity_main_recycler_view)
    RecyclerView recyclerView;

    private Disposable searchViewTextSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newest);

        setToolbar();

        ((MyApplication) getApplication()).getAppComponent().doInjection(this);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        newestViewModel = ViewModelProviders.of(this,viewModelFactory).get(NewestViewModel.class);

        newestViewModel.getNewesetMoviesLiveData().observe(this, this::consumeNewestMovieResponse);

        newestViewModel.getNewestMovies();

        searchViewModel = ViewModelProviders.of(this,viewModelFactory).get(SearchViewModel.class);

        searchViewModel.getSearchMoviesLiveData().observe(this, this::consumeSearchMovieResponse);

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

    private void renderSearchResponse(List<Movie> movies){
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        HighestMovieAdapter adapter = new HighestMovieAdapter(this,movies);
        recyclerView.swapAdapter(adapter,true);
    }

    private void consumeSearchMovieResponse(ApiResponseMovie apiResponse) {

        switch (apiResponse.status) {

            case LOADING:
                Snackbar.make(findViewById(android.R.id.content), "Loading Movies...", Snackbar.LENGTH_SHORT)
                        .show();
                break;

            case SUCCESS:
                renderSearchResponse(apiResponse.data.getMovieList());
                break;

            case ERROR:
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                Toast.makeText(this,getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    private void consumeNewestMovieResponse(ApiResponseMovie apiResponse) {

        switch (apiResponse.status) {

            case LOADING:
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                shimmerFrameLayout.startShimmer();

                break;

            case SUCCESS:
                renderSuccessResponse(apiResponse.data.getMovieList());
                break;

            case ERROR:
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                Toast.makeText(this,getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();


        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                recyclerView.swapAdapter(adapter,true);
                return true;
            }
        });

        searchViewTextSubscription = RxSearchView.queryTextChanges(searchView)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> {
                    if (charSequence.length() > 0) {
                        searchViewModel.getSearchMovies(charSequence.toString());
                    }
                });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort:
                displaySortOptions();
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
        RxUtils.unsubscribe(searchViewTextSubscription);
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
            case FAVORITES:
                showFavoriteMovies();
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

    private void showFavoriteMovies(){
        Intent intent = new Intent(this,FavoritesActivity.class);
        startActivity(intent);
        this.finish();
    }

}
