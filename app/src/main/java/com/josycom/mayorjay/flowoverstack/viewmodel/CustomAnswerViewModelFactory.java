package com.josycom.mayorjay.flowoverstack.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.josycom.mayorjay.flowoverstack.repository.AnswerRepository;

import javax.inject.Inject;

public class CustomAnswerViewModelFactory implements ViewModelProvider.Factory {

    private String order;
    private String sortCondition;
    private String site;
    private String filter;
    private int questionId;
    private String siteKey;
    private AnswerRepository answerRepository;


    @Inject
    public CustomAnswerViewModelFactory(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    public void setInputs(int questionId, String order, String sortCondition, String site, String filter, String siteKey) {
        this.questionId = questionId;
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
        return (T) new AnswerViewModel(answerRepository, questionId, order, sortCondition, site, filter, siteKey);
    }
}
