package com.josycom.mayorjay.flowoverstack.di.module;

import com.josycom.mayorjay.flowoverstack.view.tag.TagsDialogFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class TagsDialogFragmentModule {

    @ContributesAndroidInjector
    abstract TagsDialogFragment contributeTagsDialogFragment();
}
