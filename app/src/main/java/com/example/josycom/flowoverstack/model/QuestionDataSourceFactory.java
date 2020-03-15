package com.example.josycom.flowoverstack.model;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.josycom.flowoverstack.network.ApiService;

import java.util.concurrent.Executor;

public class QuestionDataSourceFactory extends DataSource.Factory {

    private final ApiService mApiService;
    private final String mAccessToken;
    private final String mServiceName;
    private String mSortType;

    private MutableLiveData<QuestionDataSource> mMutableLiveData;

    private Executor mExecutor;

    public QuestionDataSourceFactory(ApiService apiService, String accessToken, String serviceName, String sortType, Executor executor){
        this.mApiService = apiService;
        this.mAccessToken = accessToken;
        this.mServiceName = serviceName;
        this.mSortType = sortType;
        this.mExecutor = executor;
        this.mMutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        QuestionDataSource questionDataSource = new QuestionDataSource(mApiService, mAccessToken, mServiceName, mSortType, mExecutor);
        mMutableLiveData.postValue(questionDataSource);
        return questionDataSource;
    }

    public MutableLiveData<QuestionDataSource> getMutableLiveData() {
        return mMutableLiveData;
    }
}
