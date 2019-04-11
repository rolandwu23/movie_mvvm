package com.grok.akm.movie.Room;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.grok.akm.movie.Model.pojo.Movie;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FavoriteRepository {

    private FavoriteDao favoriteDao;

    public FavoriteRepository(Application application) {
        FavoriteRoomDatabase db = FavoriteRoomDatabase.getDatabase(application);
        favoriteDao = db.favoriteDao();
    }

    public LiveData<List<Movie>> getFavorites() {

        return favoriteDao.getFavorites();
    }

    public void setFavorite (Movie movie) {
        new insertAsyncTask(favoriteDao).execute(movie);
    }

    private static class insertAsyncTask extends AsyncTask<Movie, Void, Void> {

        private FavoriteDao mAsyncTaskDao;

        insertAsyncTask(FavoriteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie... params) {
            mAsyncTaskDao.setFavorite(params[0]);
            return null;
        }
    }

    public void unFavorite(Movie movie){
        new unAsyncTask(favoriteDao).execute(movie);
    }

    public Boolean isFavorite(Movie movie){
        Boolean b = null;
        try {
            b =  new checkAsyncTask(favoriteDao).execute(movie).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return b;
    }

    private static class checkAsyncTask extends AsyncTask<Movie, Void, Boolean> {

        private FavoriteDao mAsyncTaskDao;

        checkAsyncTask(FavoriteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final Movie... params) {
            Boolean b =  mAsyncTaskDao.isFavorite(params[0].getId()).size() != 0;
            return b;
        }
    }

    private static class unAsyncTask extends AsyncTask<Movie, Void, Void> {

        private FavoriteDao mAsyncTaskDao;

        unAsyncTask(FavoriteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie... params) {
             mAsyncTaskDao.unFavorite(params[0].getId());
             return null;
        }
    }

    private Movie movieRealmObjectToMovie(FavoriteRoom room){
        Movie movie = new Movie();
        movie.setId(room.getId());
        movie.setOverview(room.getOverview());
        movie.setReleaseDate(room.getReleaseDate());
        movie.setPosterPath(room.getPosterPath());
        movie.setBackdropPath(room.getBackdropPath());
        movie.setTitle(room.getTitle());
        movie.setVoteAverage(room.getVoteAverage());

        return movie;
    }
}
