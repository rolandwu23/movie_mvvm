package com.grok.akm.movie;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
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
import com.grok.akm.movie.ViewModel.FragmentViewModel;
import com.grok.akm.movie.ViewModel.MovieViewModel;
import com.grok.akm.movie.ViewModel.NewestMovieViewModel;
import com.grok.akm.movie.ViewModel.PagingHighestMovieViewModel;
import com.grok.akm.movie.ViewModel.PagingMovieViewModel;
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

import io.reactivex.disposables.Disposable;

// implements SortingDialogFragment.RadioChecked

public class MainActivity extends AppCompatActivity{

    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    SortPreferences pref;

    @Inject
    FavouritesInteractor favouritesInteractor;

    PagingMovieViewModel pagingMovieViewModel;

    PagingHighestMovieViewModel pagingHighestMovieViewModel;

    MovieViewModel movieViewModel;

    NewestMovieViewModel newestMovieViewModel;

    FragmentViewModel fragmentViewModel;

    ShimmerFrameLayout shimmerFrameLayout;

    RecyclerView recyclerView;

    MoviePageListAdapter pageListAdapter;

    private Disposable searchViewTextSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setToolbar();

        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);

        ((MyApplication) getApplication()).getAppComponent().doInjection(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView = (RecyclerView) findViewById(R.id.activity_main_recycler_view);
        recyclerView.setLayoutManager(layoutManager);

        pagingMovieViewModel = ViewModelProviders.of(this, viewModelFactory).get(PagingMovieViewModel.class);

        pagingHighestMovieViewModel = ViewModelProviders.of(this,viewModelFactory).get(PagingHighestMovieViewModel.class);

        movieViewModel = ViewModelProviders.of(this,viewModelFactory).get(MovieViewModel.class);

        movieViewModel.getListLiveData().observe(this, this::consumeMovieResponse);

        newestMovieViewModel = ViewModelProviders.of(this,viewModelFactory).get(NewestMovieViewModel.class);

        newestMovieViewModel.getListLiveData().observe(this, this::consumeMovieResponse);

        fragmentViewModel = ViewModelProviders.of(this,viewModelFactory).get(FragmentViewModel.class);

        fragmentViewModel.getStatusLiveData().observe(this, this::showSortOptions);

        pageListAdapter = new MoviePageListAdapter();

        if(Constant.checkInternetConnection(this)) {
            int selectedOption = pref.getSelectedOption();
            if(selectedOption == SortType.MOST_POPULAR.getValue()){
                showMostPopularMovies();
            }else if(selectedOption == SortType.HIGHEST_RATED.getValue()){
                showHighestMovies();
            }else if(selectedOption == SortType.NEWEST.getValue()){
                showNewestMovies();
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

        pagingMovieViewModel.getListLiveData().observe(this, pageListAdapter::submitList);

        pageListAdapter.notifyDataSetChanged();

        recyclerView.swapAdapter(pageListAdapter,true);

        pagingMovieViewModel.getProgressLoadStatus().observe(this, status -> {
//                Snackbar.make(findViewById(android.R.id.content), "Loading Movies...", Snackbar.LENGTH_SHORT)
//                        .show();

            if(Objects.requireNonNull(status).equals(Status.INITIAL)){
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

        pagingHighestMovieViewModel.getListLiveData().observe(this, pageListAdapter::submitList);

        pageListAdapter.notifyDataSetChanged();

        recyclerView.swapAdapter(pageListAdapter,true);

        pagingHighestMovieViewModel.getProgressLoadStatus().observe(this, status -> {

            if(Objects.requireNonNull(status).equals(Status.INITIAL)){
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
        newestMovieViewModel.getNewestMovies();
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
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
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
                        movieViewModel.getSearchMovies(charSequence.toString());
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
//        sortingDialogFragment.setRadioChecked(this);
        sortingDialogFragment.show(getSupportFragmentManager(), "Select Quantity");
    }
    @Override
    protected void onDestroy() {
        RxUtils.unsubscribe(searchViewTextSubscription);
        super.onDestroy();
    }

//    @Override
//    public void mostPopularSelected() { showMostPopularMovies(); }
//
//    @Override
//    public void highestRatedSelected() {
//        showHighestMovies();
//    }
//
//    @Override
//    public void favouritesSelected() { showFavourties(); }
//
//    @Override
//    public void newestSelected() { showNewestMovies(); }


}
