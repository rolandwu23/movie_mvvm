package com.grok.akm.movie;

import android.arch.lifecycle.ViewModelProviders;
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
import com.grok.akm.movie.Retrofit.Status;
import com.grok.akm.movie.ViewModel.MainViewModel;
import com.grok.akm.movie.ViewModel.ViewModelFactory;
import com.grok.akm.movie.di.SortPreferences;
import com.grok.akm.movie.favourites.FavouritesInteractor;
import com.grok.akm.movie.paging.MovieAdapter;
import com.grok.akm.movie.paging.MoviePageListAdapter;
import com.grok.akm.movie.pojo.Movie;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;


public class MainActivity extends AppCompatActivity{

    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    SortPreferences pref;

    @Inject
    FavouritesInteractor favouritesInteractor;

    MainViewModel mainViewModel;

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerFrameLayout;

    @BindView(R.id.activity_main_recycler_view)
    RecyclerView recyclerView;

    MoviePageListAdapter pageListAdapter;

    private Disposable searchViewTextSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setToolbar();

        ((MyApplication) getApplication()).getAppComponent().doInjection(this);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        mainViewModel = ViewModelProviders.of(this,viewModelFactory).get(MainViewModel.class);

        mainViewModel.getSearchMoviesLiveData().observe(this, this::consumeMovieResponse);

        mainViewModel.getNewesetMoviesLiveData().observe(this, this::consumeMovieResponse);

        mainViewModel.getStatusLiveData().observe(this, this::showSortOptions);

        pageListAdapter = new MoviePageListAdapter(this);

        if(Constant.checkInternetConnection(this)) {
            int selectedOption = pref.getSelectedOption();
            if(selectedOption == SortType.MOST_POPULAR.getValue()){
                showMostPopularMovies();
            }else if(selectedOption == SortType.HIGHEST_RATED.getValue()){
                showHighestMovies();
            }else if(selectedOption == SortType.NEWEST.getValue()){
                showNewestMovies();
            }else {
                showFavourties();
            }

        }

    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.movie_guide);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    private void showMostPopularMovies(){

        mainViewModel.getMostMoviesLiveData().observe(this, pageListAdapter::submitList);

        pageListAdapter.notifyDataSetChanged();

        recyclerView.swapAdapter(pageListAdapter,true);

        mainViewModel.getMostLoadStatus().observe(this, status -> {

            if(Objects.requireNonNull(status).equals(Status.INITIAL)){
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                shimmerFrameLayout.startShimmer();
            } else if (status.equals(Status.SUCCESS)) {
               shimmerFrameLayout.stopShimmer();
               shimmerFrameLayout.setVisibility(View.GONE);
            }else if(status.equals(Status.ERROR)){
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showHighestMovies(){

        mainViewModel.getHighestMoviesLiveData().observe(this, pageListAdapter::submitList);

        pageListAdapter.notifyDataSetChanged();

        recyclerView.swapAdapter(pageListAdapter,true);

        mainViewModel.getHighestLoadStatus().observe(this, status -> {

            if(Objects.requireNonNull(status).equals(Status.INITIAL)){
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                shimmerFrameLayout.startShimmer();
            } else if (status.equals(Status.SUCCESS)) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }else if(status.equals(Status.ERROR)){
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNewestMovies(){
        mainViewModel.getNewestMovies();
    }

    private void showFavourties(){
        renderSuccessResponse(favouritesInteractor.getFavorites());
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
            case FAVORITES:
                showFavourties();
                break;
        }
    }

    private void consumeMovieResponse(ApiResponseMovie apiResponse) {

        switch (apiResponse.status) {

            case LOADING:
                Snackbar.make(findViewById(android.R.id.content), "Loading Movies...", Snackbar.LENGTH_SHORT)
                        .show();
                break;

            case SUCCESS:
                renderSuccessResponse(apiResponse.data.getMovieList());
                break;

            case ERROR:
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    private void renderSuccessResponse(List<Movie> movies){
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        MovieAdapter adapter = new MovieAdapter(this,movies);
        recyclerView.swapAdapter(adapter,true);
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
                recyclerView.swapAdapter(pageListAdapter,false);
                return true;
            }
        });

        searchViewTextSubscription = RxSearchView.queryTextChanges(searchView)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(charSequence -> {
                    if (charSequence.length() > 0) {
                        mainViewModel.getSearchMovies(charSequence.toString());
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

    @Override
    protected void onResume() {
        super.onResume();
        if(pref.getSelectedOption() == SortType.FAVORITES.getValue()){
            showFavourties();
        }
    }
}
