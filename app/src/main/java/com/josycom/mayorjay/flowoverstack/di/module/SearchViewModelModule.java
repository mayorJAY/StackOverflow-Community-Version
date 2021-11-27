package com.josycom.mayorjay.flowoverstack.di.module;

import androidx.lifecycle.ViewModel;

import com.josycom.mayorjay.flowoverstack.data.SearchRepository;
import com.josycom.mayorjay.flowoverstack.di.ViewModelKey;
import com.josycom.mayorjay.flowoverstack.ui.viewmodel.SearchViewModel;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class SearchViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    ViewModel bindSearchViewModel(SearchRepository searchRepository) {
        return new SearchViewModel(searchRepository);
    }
}
