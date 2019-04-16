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

    public void unFavorite(String id){
        new unAsyncTask(favoriteDao).execute(id);
    }

    public Boolean isFavorite(String id){
        Boolean b = null;
        try {
            b =  new checkAsyncTask(favoriteDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return b;
    }

    private static class checkAsyncTask extends AsyncTask<String, Void, Boolean> {

        private FavoriteDao mAsyncTaskDao;

        checkAsyncTask(FavoriteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final String... params) {
            return mAsyncTaskDao.isFavorite(params[0]) > 0;
        }
    }

    private static class unAsyncTask extends AsyncTask<String, Void, Void> {

        private FavoriteDao mAsyncTaskDao;

        unAsyncTask(FavoriteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
             mAsyncTaskDao.unFavorite(params[0]);
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
