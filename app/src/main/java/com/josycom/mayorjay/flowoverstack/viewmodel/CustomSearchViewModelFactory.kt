package com.josycom.mayorjay.flowoverstack.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.josycom.mayorjay.flowoverstack.repository.SearchRepository;

import javax.inject.Inject;

public class CustomSearchViewModelFactory implements ViewModelProvider.Factory {

    private SearchRepository searchRepository;

   @Inject
    public CustomSearchViewModelFactory(SearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SearchViewModel(searchRepository);
    }
}
