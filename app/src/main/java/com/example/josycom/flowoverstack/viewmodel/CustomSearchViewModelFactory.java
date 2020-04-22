package com.example.josycom.flowoverstack.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.josycom.flowoverstack.repository.SearchRepository;


public class CustomSearchViewModelFactory implements ViewModelProvider.Factory {

    private String inTitle;
    private SearchRepository searchRepository;

    public CustomSearchViewModelFactory(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SearchViewModel(searchRepository);
    }
}
