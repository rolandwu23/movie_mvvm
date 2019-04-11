package com.grok.akm.movie.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.grok.akm.movie.Model.pojo.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class FavoriteRoomDatabase extends RoomDatabase {

    public abstract FavoriteDao favoriteDao();

    private static volatile FavoriteRoomDatabase INSTANCE;

    public static FavoriteRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FavoriteRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavoriteRoomDatabase.class, "word_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}