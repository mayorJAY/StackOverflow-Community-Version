package com.josycom.mayorjay.flowoverstack.repository;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.josycom.mayorjay.flowoverstack.model.Question;
import com.josycom.mayorjay.flowoverstack.model.QuestionDataSourceFactory;
import com.josycom.mayorjay.flowoverstack.network.ApiService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class QuestionRepository {

    private ApiService apiService;
    private LiveData<PagedList<Question>> mQuestionPagedList;

    @Inject
    public QuestionRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public void init(int page, int pageSize, String order, String sortCondition, String site, String filter, String siteKey) {
        QuestionDataSourceFactory factory = new QuestionDataSourceFactory(page, pageSize, order, sortCondition, site, filter, siteKey, apiService);
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
