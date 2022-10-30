package com.josycom.mayorjay.flowoverstack.di.module;

import androidx.lifecycle.ViewModelProvider;

import com.josycom.mayorjay.flowoverstack.viewmodel.CustomSearchViewModelFactory;

import dagger.Binds;
import dagger.Module;

@Module(includes = SearchViewModelModule.class)
public abstract class SearchViewModelFactoryModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(CustomSearchViewModelFactory customSearchViewModelFactory);
}
