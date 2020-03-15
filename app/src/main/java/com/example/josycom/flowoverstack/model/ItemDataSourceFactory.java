package com.example.josycom.flowoverstack.model;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.josycom.flowoverstack.network.ApiService;

import java.util.concurrent.Executor;

public class ItemDataSourceFactory extends DataSource.Factory {

    private final ApiService mApiService;
    private final String mAccessToken;
    private final String mServiceName;
    private String mSortType;

    private MutableLiveData<ItemDataSource> mMutableLiveData;

    private Executor mExecutor;

    public ItemDataSourceFactory(ApiService apiService, String accessToken, String serviceName,String sortType, Executor executor){
        this.mApiService = apiService;
        this.mAccessToken = accessToken;
        this.mServiceName = serviceName;
        this.mSortType = sortType;
        this.mExecutor = executor;
        this.mMutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        ItemDataSource itemDataSource = new ItemDataSource(mApiService, mAccessToken, mServiceName, mSortType, mExecutor);
        mMutableLiveData.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<ItemDataSource> getMutableLiveData() {
        return mMutableLiveData;
    }
}
