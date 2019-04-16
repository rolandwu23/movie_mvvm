package com.grok.akm.movie;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.grok.akm.movie.Network.Status;
import com.grok.akm.movie.Paging.MoviePageListAdapter;
import com.grok.akm.movie.Utils.SortType;
import com.grok.akm.movie.ViewModel.FragmentViewModel;
import com.grok.akm.movie.ViewModel.HighestViewModel;
import com.grok.akm.movie.ViewModel.ViewModelFactory;
import com.grok.akm.movie.di.MyApplication;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HighestActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Inject
    ViewModelFactory viewModelFactory;

    HighestViewModel highestViewModel;

    FragmentViewModel fragmentViewModel;

    @BindView(R.id.shimmer_view_container)
    ShimmerFrameLayout shimmerFrameLayout;

    @BindView(R.id.activity_main_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    MoviePageListAdapter pageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highest);

        setToolbar();

        ((MyApplication) getApplication()).getAppComponent().doInjection(this);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        highestViewModel = ViewModelProviders.of(this,viewModelFactory).get(HighestViewModel.class);

        fragmentViewModel = ViewModelProviders.of(this,viewModelFactory).get(FragmentViewModel.class);

        fragmentViewModel.getStatusLiveData().observe(this, this::showSortOptions);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshLayout.setOnRefreshListener(this);

        init();
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.movie_guide);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    private void init(){

        pageListAdapter = new MoviePageListAdapter(this);

        highestViewModel.getHighestMoviesLiveData().observe(this, pageListAdapter::submitList);

        pageListAdapter.notifyDataSetChanged();

        recyclerView.swapAdapter(pageListAdapter,true);

        highestViewModel.getHighestLoadStatus().observe(this, status -> {

            if(Objects.requireNonNull(status).equals(Status.INITIAL)){
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                shimmerFrameLayout.startShimmer();
            } else if (status.equals(Status.SUCCESS)) {
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }else if(status.equals(Status.ERROR)){
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                Toast.makeText(this,getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        });
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
                Intent intent = new Intent(HighestActivity.this,SearchActivity.class);
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
            case NEWEST:
                showNewestMovies();
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

    private void showNewestMovies(){
        Intent intent = new Intent(this,NewestActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void showFavoriteMovies(){
        Intent intent = new Intent(this,FavoritesActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onRefresh() {
        highestViewModel.refresh();
    }
}
