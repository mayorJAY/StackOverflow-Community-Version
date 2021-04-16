package com.josycom.mayorjay.flowoverstack.di.module;

import com.josycom.mayorjay.flowoverstack.ui.activity.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class MainActivityModule {


    @ContributesAndroidInjector(modules = QuestionsFragmentModule.class)
    abstract MainActivity contributeMainAndroidInjector();

}

