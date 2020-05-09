package com.example.josycom.flowoverstack.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.josycom.flowoverstack.model.QuestionsResponse;
import com.example.josycom.flowoverstack.model.SearchResponse;
import com.example.josycom.flowoverstack.network.ApiService;
import com.example.josycom.flowoverstack.network.RestApiClient;
import com.example.josycom.flowoverstack.util.StringConstants;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {

    private MutableLiveData<SearchResponse> mResponse = new MutableLiveData<>();

    private void getQuestionsWithTextInTitle(String inTitle) {
        ApiService apiService = RestApiClient.getApiService(ApiService.class);
        mResponse.setValue(new SearchResponse(StringConstants.LOADING, null));
        Call<QuestionsResponse> call = apiService.getQuestionsWithTextInTitle(inTitle);
        call.enqueue(new Callback<QuestionsResponse>() {
            @Override
            public void onResponse(@NotNull Call<QuestionsResponse> call, @NotNull Response<QuestionsResponse> response) {
                QuestionsResponse questionsResponse = response.body();
                if (Objects.requireNonNull(questionsResponse).getItems().size() != 0) {
                    mResponse.postValue(new SearchResponse(StringConstants.LOADED, questionsResponse.getItems()));
                } else {
                    Log.d("SearchRepository", "No matching question");
                    mResponse.postValue(new SearchResponse(StringConstants.NO_MATCHING_RESULT, null));
                }
            }

            @Override
            public void onFailure(@NotNull Call<QuestionsResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
                mResponse.postValue(new SearchResponse(StringConstants.FAILED, null));
            }
        });
    }

    public MutableLiveData<SearchResponse> getResponse(String inTitle) {
        getQuestionsWithTextInTitle(inTitle);
        return mResponse;
    }
}
