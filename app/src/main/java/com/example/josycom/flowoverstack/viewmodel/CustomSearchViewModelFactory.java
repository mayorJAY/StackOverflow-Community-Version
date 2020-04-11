package com.example.josycom.flowoverstack.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;


public class CustomSearchViewModelFactory implements ViewModelProvider.Factory {

    private String inTitle;

    public CustomSearchViewModelFactory(String inTitle) {
        this.inTitle = inTitle;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SearchViewModel(inTitle);
    }
}
