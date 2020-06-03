package com.josycom.mayorjay.flowoverstack.repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.josycom.mayorjay.flowoverstack.model.Answer;
import com.josycom.mayorjay.flowoverstack.model.AnswerResponse;
import com.josycom.mayorjay.flowoverstack.network.ApiService;
import com.josycom.mayorjay.flowoverstack.network.RestApiClient;
import com.josycom.mayorjay.flowoverstack.util.ThreadExecutor;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswerRepository {

    private final int questionId;
    private final String order;
    private final String sortCondition;
    private final String site;
    private final String filter;
    private final String siteKey;
    private MutableLiveData<List<Answer>> mAnswers = new MutableLiveData<>();

    public AnswerRepository(int questionId, String order, String sortCondition, String site, String filter, String siteKey) {
        this.questionId = questionId;
        this.order = order;
        this.sortCondition = sortCondition;
        this.site = site;
        this.filter = filter;
        this.siteKey = siteKey;
        getAnswersToQuestion();
    }

    private void getAnswersToQuestion() {
        ThreadExecutor.mExecutor.execute(() -> {
            ApiService apiService = RestApiClient.getApiService(ApiService.class);
            Call<AnswerResponse> call = apiService.getAnswersToQuestion(questionId, order, sortCondition, site, filter, siteKey);
            call.enqueue(new Callback<AnswerResponse>() {
                @Override
                public void onResponse(@NotNull Call<AnswerResponse> call, @NotNull Response<AnswerResponse> response) {
                    AnswerResponse answerResponse = response.body();
                    if (answerResponse != null) {
                        mAnswers.setValue(answerResponse.getItems());
                    }
                }

                @Override
                public void onFailure(@NotNull Call<AnswerResponse> call, @NotNull Throwable t) {
                    t.printStackTrace();
                }
            });
        });
    }

    public LiveData<List<Answer>> getAnswers() {
        return mAnswers;
    }
}
