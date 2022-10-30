package com.josycom.mayorjay.flowoverstack.di.module;

import androidx.lifecycle.ViewModelProvider;

import com.josycom.mayorjay.flowoverstack.viewmodel.CustomAnswerViewModelFactory;

import dagger.Binds;
import dagger.Module;

@Module(includes = AnswerViewModelModule.class)
public abstract class AnswerViewModelFactoryModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(CustomAnswerViewModelFactory customAnswerViewModelFactory);
}
