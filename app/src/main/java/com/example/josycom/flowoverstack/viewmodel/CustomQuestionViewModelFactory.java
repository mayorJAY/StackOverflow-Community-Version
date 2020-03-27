package com.example.josycom.flowoverstack.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CustomQuestionViewModelFactory implements ViewModelProvider.Factory {

    private int page;
    private int pageSize;
    private String order;
    private String sortCondition;
    private String site;
    private String filter;

    public CustomQuestionViewModelFactory(int page, int pageSize, String order, String sortCondition, String site, String filter){
        this.page = page;
        this.pageSize = pageSize;
        this.order = order;
        this.sortCondition = sortCondition;
        this.site = site;
        this.filter = filter;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new QuestionViewModel(page, pageSize, order, sortCondition, site, filter);
    }
}
