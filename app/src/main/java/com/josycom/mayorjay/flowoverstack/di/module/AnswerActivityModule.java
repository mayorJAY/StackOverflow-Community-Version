package com.josycom.mayorjay.flowoverstack.di.module;

import com.josycom.mayorjay.flowoverstack.ui.AnswerActivity;
import com.josycom.mayorjay.flowoverstack.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class AnswerActivityModule {


    @ContributesAndroidInjector
    abstract AnswerActivity contributeActivityAndroidInjector();

}