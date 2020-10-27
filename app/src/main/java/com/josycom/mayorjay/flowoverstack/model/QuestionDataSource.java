package com.josycom.mayorjay.flowoverstack.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.josycom.mayorjay.flowoverstack.network.ApiService;
import com.josycom.mayorjay.flowoverstack.network.RestApiClient;
import com.josycom.mayorjay.flowoverstack.util.AppConstants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionDataSource extends PageKeyedDataSource<Integer, Question> implements AppConstants {

    private final int page;
    private final int pageSize;
    private final String order;
    private final String sortCondition;
    private final String site;
    private final String filter;
    private final String siteKey;
    private ApiService apiService;
    private MutableLiveData<String> networkState;

    QuestionDataSource(int page, int pageSize, String order, String sortCondition,
                       String site, String filter, String siteKey, ApiService apiService) {
        this.page = page;
        this.pageSize = pageSize;
        this.order = order;
        this.sortCondition = sortCondition;
        this.site = site;
        this.filter = filter;
        this.siteKey = siteKey;
        this.apiService = apiService;
        networkState = new MutableLiveData<>();
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Question> callback) {
        networkState.postValue(AppConstants.LOADING);
        Call<QuestionsResponse> call = apiService.getQuestionsForAll(page, pageSize, order, sortCondition, site, filter, siteKey);
        call.enqueue(new Callback<QuestionsResponse>() {
            @Override
            public void onResponse(@NotNull Call<QuestionsResponse> call, @NotNull Response<QuestionsResponse> response) {
                QuestionsResponse apiResponse = response.body();
                if (apiResponse != null) {
                    networkState.setValue(AppConstants.LOADED);
                    List<Question> responseItems = apiResponse.getItems();
                    callback.onResult(responseItems, null, page + 1);
                } else {
                    networkState.setValue(AppConstants.FAILED);
                }
            }

            @Override
            public void onFailure(@NotNull Call<QuestionsResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
                networkState.setValue(AppConstants.FAILED);
            }
        });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Question> callback) {
        Call<QuestionsResponse> call = apiService.getQuestionsForAll(params.key, pageSize, order, sortCondition, site, filter, siteKey);
        call.enqueue(new Callback<QuestionsResponse>() {
            @Override
            public void onResponse(@NotNull Call<QuestionsResponse> call, @NotNull Response<QuestionsResponse> response) {
                QuestionsResponse apiResponse = response.body();
                if (apiResponse != null) {
                    networkState.setValue(AppConstants.LOADED);
                    List<Question> responseItems = apiResponse.getItems();
                    int key;
                    if (params.key > 1) {
                        key = params.key - 1;
                    } else {
                        key = 0;
                    }
                    callback.onResult(responseItems, key);
                } else {
                    networkState.setValue(AppConstants.FAILED);
                }
            }

            @Override
            public void onFailure(@NotNull Call<QuestionsResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
                networkState.setValue(AppConstants.FAILED);
            }
        });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Question> callback) {
        Call<QuestionsResponse> call = apiService.getQuestionsForAll(params.key, pageSize, order, sortCondition, site, filter, siteKey);
        call.enqueue(new Callback<QuestionsResponse>() {
            @Override
            public void onResponse(@NotNull Call<QuestionsResponse> call, @NotNull Response<QuestionsResponse> response) {
                QuestionsResponse apiResponse = response.body();
                if (apiResponse != null) {
                    networkState.setValue(AppConstants.LOADED);
                    List<Question> responseItems = apiResponse.getItems();
                    callback.onResult(responseItems, params.key + 1);
                } else {
                    networkState.setValue(AppConstants.FAILED);
                }
            }

            @Override
            public void onFailure(@NotNull Call<QuestionsResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
                networkState.setValue(AppConstants.FAILED);
            }
        });
    }

    public MutableLiveData<String> getNetworkState() {
        return networkState;
    }
}