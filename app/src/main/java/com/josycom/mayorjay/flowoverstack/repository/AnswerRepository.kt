package com.josycom.mayorjay.flowoverstack.repository;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.josycom.mayorjay.flowoverstack.model.Answer;
import com.josycom.mayorjay.flowoverstack.model.AnswerResponse;
import com.josycom.mayorjay.flowoverstack.network.ApiService;
import com.josycom.mayorjay.flowoverstack.util.ThreadExecutor;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class AnswerRepository {

    private int questionId;
    private String order;
    private String sortCondition;
    private String site;
    private String filter;
    private String siteKey;
    private ApiService apiService;
    private MutableLiveData<List<Answer>> mAnswers = new MutableLiveData<>();

    @Inject
    public AnswerRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public void init(int questionId, String order, String sortCondition, String site, String filter, String siteKey) {
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
