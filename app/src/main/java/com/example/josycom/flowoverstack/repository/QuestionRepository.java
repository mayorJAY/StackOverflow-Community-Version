package com.example.josycom.flowoverstack.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.model.QuestionDataSourceFactory;

public class QuestionRepository {
    private int page;
    private int pageSize;
    private String order;
    private String sortCondition;
    private String site;
    private String filter;
    private LiveData<PagedList<Question>> mQuestionPagedList;

    public QuestionRepository(int page, int pageSize, String order, String sortCondition, String site, String filter) {
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
