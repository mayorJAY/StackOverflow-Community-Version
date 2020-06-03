package com.josycom.mayorjay.flowoverstack.model;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class QuestionDataSourceFactory extends DataSource.Factory<Integer, Question> {

    private static MutableLiveData<QuestionDataSource> questionLiveDataSource = new MutableLiveData<>();
    private final int page;
    private final int pageSize;
    private final String order;
    private final String sortCondition;
    private final String site;
    private final String filter;
    private final String siteKey;

    public QuestionDataSourceFactory(int page, int pageSize, String order, String sortCondition, String site, String filter, String siteKey) {
        this.page = page;
        this.pageSize = pageSize;
        this.order = order;
        this.sortCondition = sortCondition;
        this.site = site;
        this.filter = filter;
        this.siteKey = siteKey;
    }

    public static MutableLiveData<QuestionDataSource> getQuestionLiveDataSource() {
        return questionLiveDataSource;
    }

    @Override
    public DataSource<Integer, Question> create() {
        QuestionDataSource questionDataSource = new QuestionDataSource(page, pageSize, order, sortCondition, site, filter, siteKey);
        questionLiveDataSource.postValue(questionDataSource);
        return questionDataSource;
    }
}
