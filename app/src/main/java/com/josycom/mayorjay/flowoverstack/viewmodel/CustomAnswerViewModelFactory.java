package com.josycom.mayorjay.flowoverstack.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CustomAnswerViewModelFactory implements ViewModelProvider.Factory {

    private final String order;
    private final String sortCondition;
    private final String site;
    private final String filter;
    private int questionId;
    private final String siteKey;


    public CustomAnswerViewModelFactory(int questionId, String order, String sortCondition, String site, String filter, String siteKey) {
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
        return (T) new AnswerViewModel(questionId, order, sortCondition, site, filter, siteKey);
    }
}
