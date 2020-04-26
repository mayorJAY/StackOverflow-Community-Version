package com.example.josycom.flowoverstack.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.model.QuestionDataSource;
import com.example.josycom.flowoverstack.model.QuestionDataSourceFactory;
import com.example.josycom.flowoverstack.repository.QuestionRepository;

import java.util.Objects;

public class QuestionViewModel extends ViewModel {

    private LiveData<PagedList<Question>> mQuestionPagedList;
    private LiveData<String> networkState;

    QuestionViewModel(int page, int pageSize, String order, String sortCondition, String site, String filter) {
        QuestionRepository questionRepository = new QuestionRepository(page, pageSize, order, sortCondition, site, filter);
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
