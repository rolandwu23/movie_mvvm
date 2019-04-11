package com.grok.akm.movie.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.grok.akm.movie.Utils.SortType;

public class FragmentViewModel extends ViewModel {

    private MutableLiveData<SortType> statusLiveData;

    public FragmentViewModel(){
        statusLiveData = new MutableLiveData<>();
    }

    public void setStatusLiveData(SortType status){
        statusLiveData.setValue(status);
    }

    public MutableLiveData<SortType> getStatusLiveData(){
        return statusLiveData;
    }
}
