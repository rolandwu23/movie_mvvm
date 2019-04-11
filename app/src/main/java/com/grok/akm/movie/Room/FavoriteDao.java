package com.grok.akm.movie.Room;


import android.arch.lifecycle.LiveData;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.grok.akm.movie.Model.pojo.Movie;

import java.util.List;

@Dao
public interface FavoriteDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void setFavorite(Movie movie);

    @Query("SELECT * from favorite_table ORDER BY id ASC")
    LiveData<List<Movie>> getFavorites();

    @Query("DELETE FROM favorite_table where id == :Id")
    void unFavorite(String Id);

    @Query("SELECT * from favorite_table where id == :Id")
    List<Movie> isFavorite(String Id);

    @Query("DELETE FROM favorite_table")
    void deleteAll();
}

