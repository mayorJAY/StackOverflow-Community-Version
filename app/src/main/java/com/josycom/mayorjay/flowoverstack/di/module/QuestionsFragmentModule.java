package com.josycom.mayorjay.flowoverstack.di.module;

import com.josycom.mayorjay.flowoverstack.view.home.QuestionsFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class QuestionsFragmentModule {

    @ContributesAndroidInjector
    abstract QuestionsFragment contributeQuestionsByActivityFragment();
}
