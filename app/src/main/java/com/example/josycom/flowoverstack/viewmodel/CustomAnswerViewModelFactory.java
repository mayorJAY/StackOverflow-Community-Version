package com.example.josycom.flowoverstack.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CustomAnswerViewModelFactory implements ViewModelProvider.Factory {

    private int questionId;
    private final String order;
    private final String sortCondition;
    private final String site;
    private final String filter;


    public CustomAnswerViewModelFactory(int questionId, String order, String sortCondition, String site, String filter){
        this.questionId = questionId;
        this.order = order;
        this.sortCondition = sortCondition;
        this.site = site;
        this.filter = filter;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AnswerViewModel(questionId, order, sortCondition, site, filter);
    }
}
