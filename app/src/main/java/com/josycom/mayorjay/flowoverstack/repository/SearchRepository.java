package com.josycom.mayorjay.flowoverstack.repository;


import androidx.lifecycle.MutableLiveData;

import com.josycom.mayorjay.flowoverstack.model.QuestionsResponse;
import com.josycom.mayorjay.flowoverstack.model.SearchResponse;
import com.josycom.mayorjay.flowoverstack.network.ApiService;
import com.josycom.mayorjay.flowoverstack.network.RestApiClient;
import com.josycom.mayorjay.flowoverstack.util.StringConstants;
import com.josycom.mayorjay.flowoverstack.util.ThreadExecutor;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {

    private MutableLiveData<SearchResponse> mResponse = new MutableLiveData<>();

    private void getQuestionsWithTextInTitle(String inTitle) {
        ApiService apiService = RestApiClient.getApiService(ApiService.class);
        mResponse.postValue(new SearchResponse(StringConstants.LOADING, null));
        Call<QuestionsResponse> call = apiService.getQuestionsWithTextInTitle(inTitle);
        call.enqueue(new Callback<QuestionsResponse>() {
            @Override
            public void onResponse(@NotNull Call<QuestionsResponse> call, @NotNull Response<QuestionsResponse> response) {
                QuestionsResponse questionsResponse = response.body();
                if (Objects.requireNonNull(questionsResponse).getItems().size() != 0) {
                    mResponse.setValue(new SearchResponse(StringConstants.LOADED, questionsResponse.getItems()));
                } else {
                    mResponse.setValue(new SearchResponse(StringConstants.NO_MATCHING_RESULT, null));
                }
            }

            @Override
            public void onFailure(@NotNull Call<QuestionsResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
                mResponse.setValue(new SearchResponse(StringConstants.FAILED, null));
            }
        });
    }

    public MutableLiveData<SearchResponse> getResponse(String inTitle) {
        ThreadExecutor.mExecutor.execute(() -> getQuestionsWithTextInTitle(inTitle));
        return mResponse;
    }
}
