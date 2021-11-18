package com.josycom.mayorjay.flowoverstack.di.module;

import androidx.lifecycle.ViewModelProvider;

import com.josycom.mayorjay.flowoverstack.ui.viewmodel.CustomTagsViewModelFactory;

import dagger.Binds;
import dagger.Module;

@Module(includes = TagsDialogViewModelModule.class)
public abstract class TagsViewModelFactoryModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(CustomTagsViewModelFactory customPopularTagViewModelFactory);
}
