package com.example.josycom.flowoverstack.model;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.example.josycom.flowoverstack.network.ApiService;
import com.example.josycom.flowoverstack.network.RestApiClient;
import com.example.josycom.flowoverstack.util.StringConstants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionDataSource extends PageKeyedDataSource<Integer, Question> implements StringConstants {

    private final int page;
    private final int pageSize;
    private final String order;
    private final String sortCondition;
    private final String site;
    private final String filter;

    QuestionDataSource(int page, int pageSize, String order, String sortCondition, String site, String filter){
        this.page = page;
        this.pageSize = pageSize;
        this.order = order;
        this.sortCondition = sortCondition;
        this.site = site;
        this.filter = filter;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Question> callback) {
        ApiService apiService = RestApiClient.getApiService(ApiService.class);
        Call<QuestionsResponse> call = apiService.getQuestionsForAll(page, pageSize, order, sortCondition, site, filter);
        call.enqueue(new Callback<QuestionsResponse>() {
            @Override
            public void onResponse(@NotNull Call<QuestionsResponse> call, @NotNull Response<QuestionsResponse> response) {
                QuestionsResponse apiResponse = response.body();
                if (apiResponse != null) {
                    List<Question> responseItems = apiResponse.getItems();
                    callback.onResult(responseItems, null, page + 1);
                }
            }

            @Override
            public void onFailure(@NotNull Call<QuestionsResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Question> callback) {
        ApiService apiService = RestApiClient.getApiService(ApiService.class);
        Call<QuestionsResponse> call = apiService.getQuestionsForAll(params.key, pageSize, order, sortCondition, site, filter);
        call.enqueue(new Callback<QuestionsResponse>() {
            @Override
            public void onResponse(@NotNull Call<QuestionsResponse> call, @NotNull Response<QuestionsResponse> response) {
                QuestionsResponse apiResponse = response.body();
                if (apiResponse != null){
                    List<Question> responseItems = apiResponse.getItems();
                    int key;
                    if (params.key > 1){
                        key = params.key - 1;
                    } else {
                        key = 0;
                    }
                    callback.onResult(responseItems, key);
                }
            }

            @Override
            public void onFailure(@NotNull Call<QuestionsResponse> call, @NotNull Throwable t) {

            }
        });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Question> callback) {
        ApiService apiService = RestApiClient.getApiService(ApiService.class);
        Call<QuestionsResponse> call = apiService.getQuestionsForAll(params.key, pageSize, order, sortCondition, site, filter);
        call.enqueue(new Callback<QuestionsResponse>() {
            @Override
            public void onResponse(@NotNull Call<QuestionsResponse> call, @NotNull Response<QuestionsResponse> response) {
                QuestionsResponse apiResponse = response.body();
                if (apiResponse != null){
                    List<Question> responseItems = apiResponse.getItems();
                    callback.onResult(responseItems, params.key + 1);
                }
            }

            @Override
            public void onFailure(@NotNull Call<QuestionsResponse> call, @NotNull Throwable t) {

            }
        });
    }
}
