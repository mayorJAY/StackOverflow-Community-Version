package com.josycom.mayorjay.flowoverstack.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.josycom.mayorjay.flowoverstack.repository.QuestionRepository;

import javax.inject.Inject;

public class CustomQuestionViewModelFactory implements ViewModelProvider.Factory {

    private int page;
    private int pageSize;
    private String order;
    private String sortCondition;
    private String site;
    private String filter;
    private String siteKey;
    private QuestionRepository questionRepository;

    @Inject
    public CustomQuestionViewModelFactory(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void setInputs(int page, int pageSize, String order, String sortCondition, String site, String filter, String siteKey) {
        this.page = page;
        this.pageSize = pageSize;
        this.order = order;
        this.sortCondition = sortCondition;
        this.site = site;
        this.filter = filter;
        this.siteKey = siteKey;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new QuestionViewModel(questionRepository, page, pageSize, order, sortCondition, site, filter, siteKey);
    }
}
