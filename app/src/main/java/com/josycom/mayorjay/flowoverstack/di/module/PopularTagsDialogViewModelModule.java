package com.josycom.mayorjay.flowoverstack.di.module;

import androidx.lifecycle.ViewModel;

import com.josycom.mayorjay.flowoverstack.data.TagRepository;
import com.josycom.mayorjay.flowoverstack.di.ViewModelKey;
import com.josycom.mayorjay.flowoverstack.ui.viewmodel.PopularTagsDialogViewModel;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class PopularTagsDialogViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(PopularTagsDialogViewModel.class)
    ViewModel bindPopularTagsDialogViewModel(int page, int pageSize, String inName, String siteKey, TagRepository tagRepository) {
        return new PopularTagsDialogViewModel(tagRepository, page, pageSize, inName, siteKey);
    }
}
