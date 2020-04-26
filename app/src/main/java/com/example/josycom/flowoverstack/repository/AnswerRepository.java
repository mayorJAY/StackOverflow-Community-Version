package com.example.josycom.flowoverstack.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.josycom.flowoverstack.model.Answer;
import com.example.josycom.flowoverstack.model.AnswerResponse;
import com.example.josycom.flowoverstack.network.ApiService;
import com.example.josycom.flowoverstack.network.RestApiClient;

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
    private MutableLiveData<List<Answer>> mAnswers = new MutableLiveData<>();

    public AnswerRepository(int questionId, String order, String sortCondition, String site, String filter){
        this.questionId = questionId;
        this.order = order;
        this.sortCondition = sortCondition;
        this.site = site;
        this.filter = filter;
        getAnswersToQuestion();
    }

    private void getAnswersToQuestion() {
        ApiService apiService = RestApiClient.getApiService(ApiService.class);
        Call<AnswerResponse> call = apiService.getAnswersToQuestion(questionId, order, sortCondition, site, filter);
        call.enqueue(new Callback<AnswerResponse>() {
            @Override
            public void onResponse(@NotNull Call<AnswerResponse> call, @NotNull Response<AnswerResponse> response) {
                AnswerResponse answerResponse = response.body();
                if (answerResponse != null) {
                    mAnswers.setValue(answerResponse.getItems());
                } else {
                    Log.d("AnswerRepository", "No answer yet");
                }
            }

            @Override
            public void onFailure(@NotNull Call<AnswerResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public LiveData<List<Answer>> getAnswers() {
        return mAnswers;
    }
}
