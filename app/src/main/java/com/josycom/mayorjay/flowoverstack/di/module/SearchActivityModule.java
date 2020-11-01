package com.josycom.mayorjay.flowoverstack.di.module;

import com.josycom.mayorjay.flowoverstack.ui.MainActivity;
import com.josycom.mayorjay.flowoverstack.ui.OcrActivity;
import com.josycom.mayorjay.flowoverstack.ui.SearchActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SearchActivityModule {

    @ContributesAndroidInjector
    abstract SearchActivity searchAndroidInjector();

    @ContributesAndroidInjector
    abstract OcrActivity ocrAndroidInjector();
}
