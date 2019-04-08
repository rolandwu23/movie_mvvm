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
            if(modelClass.isAssignableFrom(MainViewModel.class)){
                return (T) new MainViewModel(repository);
            }else if(modelClass.isAssignableFrom(DetailsViewModel.class)){
                return (T) new DetailsViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown class name");
        }
    }

