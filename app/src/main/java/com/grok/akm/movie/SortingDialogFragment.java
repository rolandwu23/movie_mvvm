package com.grok.akm.movie;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.grok.akm.movie.di.SortOptionsModule;
import com.grok.akm.movie.di.SortPreferences;

import javax.inject.Inject;


public class SortingDialogFragment extends DialogFragment implements RadioGroup.OnCheckedChangeListener {

    RadioButton mostPopular;
    RadioButton highestRated;
    RadioButton favorites;
    RadioButton newest;

    RadioGroup sortingOptionsGroup;

    RadioChecked radioChecked;

    @Inject
    SortPreferences pref;

    public static SortingDialogFragment newInstance()
    {
        return new SortingDialogFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((MyApplication) getActivity().getApplication()).getAppComponent().doInjection(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.sorting_options, null);

        mostPopular = (RadioButton) dialogView.findViewById(R.id.most_popular);
        highestRated = (RadioButton) dialogView.findViewById(R.id.highest_rated);
        favorites = (RadioButton) dialogView.findViewById(R.id.favorites);
        newest = (RadioButton) dialogView.findViewById(R.id.newest);
        sortingOptionsGroup = (RadioGroup) dialogView.findViewById(R.id.sorting_group);
        setChecked();
        sortingOptionsGroup.setOnCheckedChangeListener(this);

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(dialogView);
        dialog.setTitle(R.string.sort_by);
        dialog.show();
        return dialog;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId)
        {
            case R.id.most_popular:
                radioChecked.mostPopularSelected();
                pref.setSelectedOption(SortType.MOST_POPULAR);
                dismiss();
                break;

            case R.id.highest_rated:
                radioChecked.highestRatedSelected();
                pref.setSelectedOption(SortType.HIGHEST_RATED);
                dismiss();
                break;

            case R.id.favorites:
                radioChecked.favouritesSelected();
                pref.setSelectedOption(SortType.FAVORITES);
                dismiss();
                break;
            case R.id.newest:
                radioChecked.newestSelected();
                pref.setSelectedOption(SortType.NEWEST);
                dismiss();
                break;
        }

    }

    private void setChecked(){
        int selectedOption = pref.getSelectedOption();
        if(selectedOption == SortType.MOST_POPULAR.getValue()){
            mostPopular.setChecked(true);
        }else if(selectedOption == SortType.HIGHEST_RATED.getValue()){
            highestRated.setChecked(true);
        }else if(selectedOption == SortType.NEWEST.getValue()){
            newest.setChecked(true);
        }else{
            favorites.setChecked(true);
        }
    }

    public void setRadioChecked(RadioChecked radioChecked){
        this.radioChecked = radioChecked;
    }

    public interface RadioChecked{
        void mostPopularSelected();
        void highestRatedSelected();
        void favouritesSelected();
        void newestSelected();
    }
}
