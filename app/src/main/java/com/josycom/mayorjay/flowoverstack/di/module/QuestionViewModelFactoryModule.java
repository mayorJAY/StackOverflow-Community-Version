package com.josycom.mayorjay.flowoverstack.di.module;

import androidx.lifecycle.ViewModelProvider;

import com.josycom.mayorjay.flowoverstack.ui.viewmodel.CustomQuestionViewModelFactory;

import dagger.Binds;
import dagger.Module;

@Module(includes = QuestionViewModelModule.class)
public abstract class QuestionViewModelFactoryModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(CustomQuestionViewModelFactory customQuestionViewModelFactory);
}
