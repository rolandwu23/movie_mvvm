package com.grok.akm.movie.ViewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;


import javax.inject.Inject;


    public class ViewModelFactory implements ViewModelProvider.Factory {

        private Repository repository;

        @Inject
        public ViewModelFactory(Repository repository) {
            this.repository = repository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(PagingMovieViewModel.class)) {
                return (T) new PagingMovieViewModel(repository);
            }else if(modelClass.isAssignableFrom(MovieViewModel.class)){
                return (T) new MovieViewModel(repository);
            }else if(modelClass.isAssignableFrom(PagingHighestMovieViewModel.class)){
                return (T) new PagingHighestMovieViewModel(repository);
            }else if(modelClass.isAssignableFrom(TrailersViewModel.class)){
                return (T) new TrailersViewModel(repository);
            }else if(modelClass.isAssignableFrom(ReviewsViewModel.class)){
                return (T) new ReviewsViewModel(repository);
            }else if(modelClass.isAssignableFrom(NewestMovieViewModel.class)){
                return (T) new NewestMovieViewModel(repository);
            }else if(modelClass.isAssignableFrom(FragmentViewModel.class)){
                return (T) new FragmentViewModel();
            }
            throw new IllegalArgumentException("Unknown class name");
        }
    }

