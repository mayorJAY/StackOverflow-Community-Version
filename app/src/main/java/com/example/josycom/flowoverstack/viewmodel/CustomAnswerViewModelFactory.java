package com.example.josycom.flowoverstack.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CustomAnswerViewModelFactory implements ViewModelProvider.Factory {

    private int questionId;
    private String sortCondition;


    public CustomAnswerViewModelFactory(int questionId){
        this.questionId = questionId;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AnswerViewModel(questionId);
    }
}
