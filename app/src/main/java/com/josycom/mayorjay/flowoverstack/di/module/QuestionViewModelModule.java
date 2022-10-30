package com.josycom.mayorjay.flowoverstack.di.module;

import androidx.lifecycle.ViewModel;

import com.josycom.mayorjay.flowoverstack.data.repository.QuestionRepository;
import com.josycom.mayorjay.flowoverstack.di.ViewModelKey;
import com.josycom.mayorjay.flowoverstack.viewmodel.QuestionViewModel;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module
public class QuestionViewModelModule {

    @Provides
    @IntoMap
    @ViewModelKey(QuestionViewModel.class)
    ViewModel bindQuestionViewModel(int page, int pageSize, String order, String sortCondition, String site, String tagged, String filter, String siteKey, QuestionRepository questionRepository) {
        return new QuestionViewModel(questionRepository, page, pageSize, order, sortCondition, site, tagged, filter, siteKey);
    }
}
