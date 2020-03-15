package com.example.josycom.flowoverstack.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.josycom.flowoverstack.network.ApiService;

public class CustomViewModelFactory implements ViewModelProvider.Factory {

    private ApiService mApiService;
    private String mAccessToken;
    private String mServiceName;
    private String mSortType;

    public CustomViewModelFactory(ApiService apiService, String accessToken, String serviceName, String sortType){
        this.mApiService = apiService;
        this.mAccessToken = accessToken;
        this.mServiceName = serviceName;
        this.mSortType = sortType;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new QuestionViewModel(mApiService, mAccessToken, mServiceName, mSortType);
    }
}
