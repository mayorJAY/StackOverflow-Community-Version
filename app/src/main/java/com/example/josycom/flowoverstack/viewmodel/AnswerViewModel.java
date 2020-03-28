package com.example.josycom.flowoverstack.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.josycom.flowoverstack.model.Answer;
import com.example.josycom.flowoverstack.model.AnswerResponse;
import com.example.josycom.flowoverstack.network.ApiService;
import com.example.josycom.flowoverstack.network.RestApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnswerViewModel extends ViewModel {

    private LiveData<List<Answer>> mAnswerLiveData;

    public AnswerViewModel(int questionId){
        this.mAnswerLiveData = new LiveData<List<Answer>>() {
            @Override
            protected void setValue(List<Answer> value) {
                super.setValue(value);
            }
        };
        ApiService apiService = RestApiClient.getApiService(ApiService.class);
        Call<AnswerResponse> call = apiService.getAnswersToQuestion(questionId);
        call.enqueue(new Callback<AnswerResponse>() {
            @Override
            public void onResponse(Call<AnswerResponse> call, Response<AnswerResponse> response) {
                AnswerResponse answerResponse = response.body();
                if (answerResponse != null){
                    mAnswerLiveData = answerResponse.getItems();
                }
            }

            @Override
            public void onFailure(Call<AnswerResponse> call, Throwable t) {
                t.printStackTrace();

            }
        });

    }

    public void setAnswerLiveData(LiveData<List<Answer>> answerLiveData) {
        mAnswerLiveData = answerLiveData;
    }

    public LiveData<List<Answer>> getAnswerLiveData() {
        return mAnswerLiveData;
    }
}
