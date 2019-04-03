package com.grok.akm.movie;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.grok.akm.movie.Retrofit.Status;
import com.grok.akm.movie.ViewModel.PagingMovieViewModel;
import com.grok.akm.movie.ViewModel.ViewModelFactory;
import com.grok.akm.movie.paging.MoviePageListAdapter;

import java.util.Objects;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    ViewModelFactory viewModelFactory;

    PagingMovieViewModel pagingMovieViewModel;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = Constant.getProgressDialog(this, "Loading Movies...");

        ((MyApplication) getApplication()).getAppComponent().doInjection(this);

        pagingMovieViewModel = ViewModelProviders.of(this, viewModelFactory).get(PagingMovieViewModel.class);

        if(Constant.checkInternetConnection(this)) {
            init();
        }

    }


    private void init(){
        MoviePageListAdapter adapter = new MoviePageListAdapter();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_main_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        pagingMovieViewModel.getListLiveData().observe(this, adapter::submitList);

        pagingMovieViewModel.getProgressLoadStatus().observe(this, status -> {
            if (Objects.requireNonNull(status).equals(Status.LOADING)) {
                Snackbar.make(findViewById(android.R.id.content), "Loading Movies...", Snackbar.LENGTH_SHORT)
                        .show();
            }else if(status.equals(Status.INITIAL)){
                progressDialog.show();
            } else if (status.equals(Status.SUCCESS)) {
               progressDialog.dismiss();
            }else if(status.equals(Status.ERROR)){
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void consumeMovieResponse(ApiResponseMovie apiResponse) {

        switch (apiResponse.status) {

            case LOADING:
                progressDialog.show();
                break;

            case SUCCESS:
                progressDialog.dismiss();
                break;

            case ERROR:
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this,getResources().getString(R.string.errorString), Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }


    /*
     * method to handle success response
     * */


}
