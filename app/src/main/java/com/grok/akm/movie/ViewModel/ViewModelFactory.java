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
            }else if(modelClass.isAssignableFrom(NewestViewModel.class)){
                return (T) new NewestViewModel(repository);
            }else if(modelClass.isAssignableFrom(FragmentViewModel.class)){
                return (T) new FragmentViewModel();
            }else if(modelClass.isAssignableFrom(SearchViewModel.class)){
                return (T) new SearchViewModel(repository);
            }else if(modelClass.isAssignableFrom(HighestViewModel.class)){
                return (T) new HighestViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown class name");
        }
    }

