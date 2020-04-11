package com.example.josycom.flowoverstack.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.model.QuestionsResponse;
import com.example.josycom.flowoverstack.network.ApiService;
import com.example.josycom.flowoverstack.network.RestApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {

    private String inTitle;
    private MutableLiveData<List<Question>> mQuestions = new MutableLiveData<>();

    public SearchRepository(String inTitle){
        this.inTitle = inTitle;
        getQuestionsWithTextInTitle();
    }

    private void getQuestionsWithTextInTitle() {
        ApiService apiService = RestApiClient.getApiService(ApiService.class);
        Call<QuestionsResponse> call = apiService.getQuestionsWithTextInTitle(inTitle);
        call.enqueue(new Callback<QuestionsResponse>() {
            @Override
            public void onResponse(Call<QuestionsResponse> call, Response<QuestionsResponse> response) {
                QuestionsResponse questionsResponse = response.body();
                if (questionsResponse != null) {
                    mQuestions.setValue(questionsResponse.getItems());
                } else {
                    Log.d("SearchRepository", "No matching question");
                }
            }

            @Override
            public void onFailure(Call<QuestionsResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public LiveData<List<Question>> getQuestions() {
        return mQuestions;
    }
}
