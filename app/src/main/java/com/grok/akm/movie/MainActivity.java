package com.grok.akm.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.grok.akm.movie.Utils.Constant;
import com.grok.akm.movie.Utils.SortType;
import com.grok.akm.movie.di.MyApplication;
import com.grok.akm.movie.di.SortPreferences;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    SortPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MyApplication) getApplication()).getAppComponent().doInjection(this);

        if(Constant.checkInternetConnection(this)) {
            int selectedOption = pref.getSelectedOption();
            if(selectedOption == SortType.MOST_POPULAR.getValue()){
                showMostPopularMovies();
            }else if(selectedOption == SortType.HIGHEST_RATED.getValue()){
                showHighestMovies();
            }else if(selectedOption == SortType.NEWEST.getValue()){
                showNewestMovies();
            }else{
                showFavourites();
            }
        }
    }

    private void showMostPopularMovies(){
        Intent intent = new Intent(this,MostActivity.class);
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

    private void showFavourites(){
        Intent intent = new Intent(this,FavoritesActivity.class);
        startActivity(intent);
        this.finish();
    }
}
