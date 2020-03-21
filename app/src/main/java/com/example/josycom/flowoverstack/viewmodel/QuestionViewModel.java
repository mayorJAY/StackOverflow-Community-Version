package com.example.josycom.flowoverstack.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.model.QuestionDataSource;
import com.example.josycom.flowoverstack.model.QuestionDataSourceFactory;
import com.example.josycom.flowoverstack.util.StringConstants;

public class QuestionViewModel extends ViewModel {
    private LiveData<PagedList<Question>> mQuestionPagedList;

    public QuestionViewModel() {
        init();
    }

    private void init() {
        QuestionDataSourceFactory factory = new QuestionDataSourceFactory();
        LiveData<QuestionDataSource> dataSource = factory.questionLiveDataSource;
        PagedList.Config pageConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(StringConstants.PAGE_SIZE)
                .build();
        mQuestionPagedList = new LivePagedListBuilder<>(factory, pageConfig).build();
    }

    public LiveData<PagedList<Question>> getQuestionPagedList() {
        return mQuestionPagedList;
    }
}
