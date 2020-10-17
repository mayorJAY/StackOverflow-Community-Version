package com.josycom.mayorjay.flowoverstack.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.josycom.mayorjay.flowoverstack.model.Question;
import com.josycom.mayorjay.flowoverstack.model.QuestionDataSourceFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class QuestionRepository {
    private int page;
    private int pageSize;
    private String order;
    private String sortCondition;
    private String site;
    private String filter;
    private final String siteKey;
    private LiveData<PagedList<Question>> mQuestionPagedList;


    public QuestionRepository(int page, int pageSize, String order, String sortCondition, String site, String filter, String siteKey) {
        this.page = page;
        this.pageSize = pageSize;
        this.order = order;
        this.sortCondition = sortCondition;
        this.site = site;
        this.filter = filter;
        this.siteKey = siteKey;
        init();
    }

    private void init() {
        QuestionDataSourceFactory factory = new QuestionDataSourceFactory(page, pageSize, order, sortCondition, site, filter, siteKey);
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
