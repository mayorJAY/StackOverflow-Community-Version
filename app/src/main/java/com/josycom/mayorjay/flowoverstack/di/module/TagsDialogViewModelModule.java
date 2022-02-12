package com.josycom.mayorjay.flowoverstack.di.module;

import androidx.lifecycle.ViewModel;

import com.josycom.mayorjay.flowoverstack.data.TagRepository;
import com.josycom.mayorjay.flowoverstack.di.ViewModelKey;
import com.josycom.mayorjay.flowoverstack.ui.viewmodel.TagsDialogViewModel;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class TagsDialogViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(TagsDialogViewModel.class)
    ViewModel bindTagsDialogViewModel(int page, int pageSize, String siteKey, TagRepository tagRepository) {
        return new TagsDialogViewModel(tagRepository, page, pageSize, siteKey);
    }
}
