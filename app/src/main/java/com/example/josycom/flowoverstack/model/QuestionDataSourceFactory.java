package com.example.josycom.flowoverstack.model;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class QuestionDataSourceFactory extends DataSource.Factory<Integer, Question> {

    public MutableLiveData<QuestionDataSource> questionLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource<Integer, Question> create() {
        QuestionDataSource questionDataSource = new QuestionDataSource();
        questionLiveDataSource.postValue(questionDataSource);
        return questionDataSource;
    }
}
