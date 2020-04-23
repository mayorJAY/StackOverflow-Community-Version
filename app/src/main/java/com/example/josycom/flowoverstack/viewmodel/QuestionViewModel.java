package com.example.josycom.flowoverstack.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.model.QuestionDataSource;
import com.example.josycom.flowoverstack.model.QuestionDataSourceFactory;
import com.example.josycom.flowoverstack.repository.QuestionRepository;

import java.util.Objects;

public class QuestionViewModel extends ViewModel {

    private LiveData<PagedList<Question>> mQuestionPagedList;

    QuestionViewModel(int page, int pageSize, String order, String sortCondition, String site, String filter) {
        QuestionRepository questionRepository = new QuestionRepository(page, pageSize, order, sortCondition, site, filter);
        mQuestionPagedList = questionRepository.getQuestionPagedList();
    }

    public LiveData<PagedList<Question>> getQuestionPagedList() {
        return mQuestionPagedList;
    }

    public void refresh() {
        Objects.requireNonNull(QuestionDataSourceFactory.questionLiveDataSource.getValue()).invalidate();
    }
}
