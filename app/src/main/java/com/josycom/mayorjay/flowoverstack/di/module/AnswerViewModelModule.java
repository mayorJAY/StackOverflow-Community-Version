package com.josycom.mayorjay.flowoverstack.di.module;

import androidx.lifecycle.ViewModel;

import com.josycom.mayorjay.flowoverstack.di.ViewModelKey;
import com.josycom.mayorjay.flowoverstack.repository.AnswerRepository;
import com.josycom.mayorjay.flowoverstack.viewmodel.AnswerViewModel;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class AnswerViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(AnswerViewModel.class)
    ViewModel bindAnswerViewModel(AnswerRepository answerRepository, int questionId, String order, String sortCondition, String site, String filter, String siteKey) {
        return new AnswerViewModel(answerRepository, questionId, order, sortCondition, site, filter, siteKey);
    }
}
