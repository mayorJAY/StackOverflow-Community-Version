package com.josycom.mayorjay.flowoverstack.di.module;

import com.josycom.mayorjay.flowoverstack.view.ocr.OcrActivity;
import com.josycom.mayorjay.flowoverstack.view.search.SearchActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SearchActivityModule {

    @ContributesAndroidInjector
    abstract SearchActivity searchAndroidInjector();

    @ContributesAndroidInjector
    abstract OcrActivity ocrAndroidInjector();
}
