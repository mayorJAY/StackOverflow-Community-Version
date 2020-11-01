package com.josycom.mayorjay.flowoverstack.repository;


import androidx.lifecycle.MutableLiveData;

import com.josycom.mayorjay.flowoverstack.model.QuestionsResponse;
import com.josycom.mayorjay.flowoverstack.model.SearchResponse;
import com.josycom.mayorjay.flowoverstack.network.ApiService;
import com.josycom.mayorjay.flowoverstack.network.RestApiClient;
import com.josycom.mayorjay.flowoverstack.util.AppConstants;
import com.josycom.mayorjay.flowoverstack.util.ThreadExecutor;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class SearchRepository {

    private ApiService apiService;
    private MutableLiveData<SearchResponse> mResponse = new MutableLiveData<>();

    @Inject
    public SearchRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    private void getQuestionsWithTextInTitle(String inTitle) {
        mResponse.postValue(new SearchResponse(AppConstants.LOADING, null));
        Call<QuestionsResponse> call = apiService.getQuestionsWithTextInTitle(inTitle);
        call.enqueue(new Callback<QuestionsResponse>() {
            @Override
            public void onResponse(@NotNull Call<QuestionsResponse> call, @NotNull Response<QuestionsResponse> response) {
                QuestionsResponse questionsResponse = response.body();
                if (Objects.requireNonNull(questionsResponse).getItems().size() != 0) {
                    mResponse.setValue(new SearchResponse(AppConstants.LOADED, questionsResponse.getItems()));
                } else {
                    mResponse.setValue(new SearchResponse(AppConstants.NO_MATCHING_RESULT, null));
                }
            }

            @Override
            public void onFailure(@NotNull Call<QuestionsResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
                mResponse.setValue(new SearchResponse(AppConstants.FAILED, null));
            }
        });
    }

    public MutableLiveData<SearchResponse> getResponse(String inTitle) {
        ThreadExecutor.mExecutor.execute(() -> getQuestionsWithTextInTitle(inTitle));
        return mResponse;
    }
}
