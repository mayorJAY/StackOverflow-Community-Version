package com.josycom.mayorjay.flowoverstack.di.module;

import androidx.lifecycle.ViewModelProvider;

import com.josycom.mayorjay.flowoverstack.ui.viewmodel.CustomPopularTagsViewModelFactory;

import dagger.Binds;
import dagger.Module;

@Module(includes = PopularTagsDialogViewModelModule.class)
public abstract class PopularTagsViewModelFactoryModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(CustomPopularTagsViewModelFactory customPopularTagViewModelFactory);
}
