package com.josycom.mayorjay.flowoverstack.di.module;

import androidx.lifecycle.ViewModel;

import com.josycom.mayorjay.flowoverstack.data.repository.SearchRepository;
import com.josycom.mayorjay.flowoverstack.di.ViewModelKey;
import com.josycom.mayorjay.flowoverstack.viewmodel.SearchViewModel;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class SearchViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    ViewModel bindSearchViewModel(int page, int pageSize, SearchRepository searchRepository) {
        return new SearchViewModel(page, pageSize, searchRepository);
    }
}
