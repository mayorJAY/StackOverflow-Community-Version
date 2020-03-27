package com.example.josycom.flowoverstack.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.model.QuestionDataSource;
import com.example.josycom.flowoverstack.model.QuestionDataSourceFactory;

public class QuestionViewModel extends ViewModel {
    private int page;
    private int pageSize;
    private String order;
    private String sortCondition;
    private String site;
    private String filter;
    private LiveData<PagedList<Question>> mQuestionPagedList;

    public QuestionViewModel(int page, int pageSize, String order, String sortCondition, String site, String filter) {
        this.page = page;
        this.pageSize = pageSize;
        this.order = order;
        this.sortCondition = sortCondition;
        this.site = site;
        this.filter = filter;
        init();
    }

    private void init() {
        QuestionDataSourceFactory factory = new QuestionDataSourceFactory(page, pageSize, order, sortCondition, site, filter);
        LiveData<QuestionDataSource> dataSource = factory.questionLiveDataSource;
        PagedList.Config pageConfig = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(pageSize)
                .build();
        mQuestionPagedList = new LivePagedListBuilder<>(factory, pageConfig).build();
    }

    public LiveData<PagedList<Question>> getQuestionPagedList() {
        return mQuestionPagedList;
    }
}
