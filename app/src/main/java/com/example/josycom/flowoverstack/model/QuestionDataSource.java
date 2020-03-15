package com.example.josycom.flowoverstack.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.example.josycom.flowoverstack.network.ApiResponse;
import com.example.josycom.flowoverstack.network.ApiResponseListener;
import com.example.josycom.flowoverstack.network.ApiService;
import com.example.josycom.flowoverstack.network.NetworkState;
import com.example.josycom.flowoverstack.util.StringConstants;

import java.util.concurrent.Executor;

import retrofit2.Call;

public class QuestionDataSource extends PageKeyedDataSource<Integer, Question> implements StringConstants {

    private static final int FIRST_PAGE = 1;

    private final ApiService mApiService;
    private final String mAccessToken;
    private final String mServiceName;
    private String mSortType;

    private MutableLiveData<NetworkState> mNetworkState;
    private MutableLiveData<NetworkState> mInitialLoading;
    private Executor mRetryExecutor;

    public QuestionDataSource(ApiService apiService, String accessToken, String serviceName, String sortType, Executor retryExecutor){
        this.mApiService = apiService;
        this.mAccessToken = accessToken;
        this.mServiceName = serviceName;
        this.mSortType = sortType;
        this.mRetryExecutor = retryExecutor;
        this.mNetworkState = new MutableLiveData<>();
        this.mInitialLoading = new MutableLiveData<>();
    }

    public MutableLiveData<NetworkState> getNetworkState() {
        return mNetworkState;
    }

    public MutableLiveData<NetworkState> getInitialLoading() {
        return mInitialLoading;
    }

    private Call<QuestionsResponse> getRetrofitRequestCall(int page){
        return mApiService.getQuestionsForAll(page, PAGE_SIZE, ORDER_DESCENDING, mSortType, SITE, FILTER);
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Question> callback) {
        mInitialLoading.postValue(NetworkState.LOADING);
        mNetworkState.postValue(NetworkState.LOADING);
        Call<QuestionsResponse> call = getRetrofitRequestCall(FIRST_PAGE);
        ApiResponse.sendRequest(call, mServiceName, new ApiResponseListener() {
            @Override
            public void onSuccess(String strApiName, Object response) {
                mInitialLoading.postValue(NetworkState.LOADED);
                mNetworkState.postValue(NetworkState.LOADED);
                QuestionsResponse questionsResponse = (QuestionsResponse) response;
                callback.onResult(questionsResponse.getItems(), null, FIRST_PAGE + 1);
            }

            @Override
            public void onError(String strApiName, ErrorModel errorModel) {
                mInitialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, errorModel.getErrorId(), errorModel.getErrorMessage(), mServiceName));
                mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorModel.getErrorId(), errorModel.getErrorMessage(), mServiceName));
            }

            @Override
            public void onFailure(String strApiName, String message) {
                mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED, 600, message, mServiceName));
            }
        });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Question> callback) {
        mNetworkState.postValue(NetworkState.LOADING);
        Call<QuestionsResponse> call = getRetrofitRequestCall(params.key);
        ApiResponse.sendRequest(call, mServiceName, new ApiResponseListener() {
            @Override
            public void onSuccess(String strApiName, Object response) {
                mNetworkState.postValue(NetworkState.LOADED);
                Integer key = (params.key > 1) ? params.key - 1 : null;
                QuestionsResponse questionsResponse = (QuestionsResponse) response;
                callback.onResult(questionsResponse.getItems(), key);
            }

            @Override
            public void onError(String strApiName, ErrorModel errorModel) {
                mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorModel.getErrorId(), errorModel.getErrorMessage(), mServiceName));
            }

            @Override
            public void onFailure(String strApiName, String message) {
                mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED, 600, message, mServiceName));
            }
        });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Question> callback) {
        mNetworkState.postValue(NetworkState.LOADING);
        Call<QuestionsResponse> call = getRetrofitRequestCall(params.key);
        ApiResponse.sendRequest(call, mServiceName, new ApiResponseListener() {
            @Override
            public void onSuccess(String strApiName, Object response) {
                mNetworkState.postValue(NetworkState.LOADED);
                QuestionsResponse questionsResponse = (QuestionsResponse) response;
                Integer key = questionsResponse.getHasMore() ? params.key + 1 : null;
                callback.onResult(questionsResponse.getItems(), key);
            }

            @Override
            public void onError(String strApiName, ErrorModel errorModel) {
                mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorModel.getErrorId(), errorModel.getErrorMessage(), mServiceName));
            }

            @Override
            public void onFailure(String strApiName, String message) {
                mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED, 600, message, mServiceName));
            }
        });
    }
}
