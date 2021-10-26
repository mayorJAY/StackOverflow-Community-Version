package com.josycom.mayorjay.flowoverstack.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.josycom.mayorjay.flowoverstack.model.Question;
import com.josycom.mayorjay.flowoverstack.model.QuestionDataSource;
import com.josycom.mayorjay.flowoverstack.model.QuestionDataSourceFactory;
import com.josycom.mayorjay.flowoverstack.repository.QuestionRepository;

import java.util.Objects;

import javax.inject.Inject;

public class QuestionViewModel extends ViewModel {

    private LiveData<PagedList<Question>> mQuestionPagedList;
    private LiveData<String> networkState;

    @Inject
    public QuestionViewModel(QuestionRepository questionRepository, int page, int pageSize, String order, String sortCondition, String site, String filter, String siteKey) {
        questionRepository.init(page, pageSize, order, sortCondition, site, filter, siteKey);
        mQuestionPagedList = questionRepository.getQuestionPagedList();
        LiveData<QuestionDataSource> liveDataSource = QuestionDataSourceFactory.getQuestionLiveDataSource();
        networkState = Transformations.switchMap(liveDataSource, QuestionDataSource::getNetworkState);
    }

    public LiveData<PagedList<Question>> getQuestionPagedList() {
        return mQuestionPagedList;
    }

    public LiveData<String> getNetworkState() {
        return networkState;
    }

    public void refresh() {
        Objects.requireNonNull(QuestionDataSourceFactory.getQuestionLiveDataSource().getValue()).invalidate();
    }
}
