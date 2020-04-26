package com.example.josycom.flowoverstack.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.model.QuestionsResponse;
import com.example.josycom.flowoverstack.network.ApiService;
import com.example.josycom.flowoverstack.network.RestApiClient;
import com.example.josycom.flowoverstack.util.StringConstants;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {

    private MutableLiveData<List<Question>> mQuestions = new MutableLiveData<>();
    private MutableLiveData<String> networkState = new MutableLiveData<>();

    private void getQuestionsWithTextInTitle(String inTitle) {
        ApiService apiService = RestApiClient.getApiService(ApiService.class);
        networkState.postValue(StringConstants.LOADING);
        Call<QuestionsResponse> call = apiService.getQuestionsWithTextInTitle(inTitle);
        call.enqueue(new Callback<QuestionsResponse>() {
            @Override
            public void onResponse(@NotNull Call<QuestionsResponse> call, @NotNull Response<QuestionsResponse> response) {
                QuestionsResponse questionsResponse = response.body();
                if (questionsResponse != null) {
                    networkState.postValue(StringConstants.LOADED);
                    mQuestions.postValue(questionsResponse.getItems());
                } else {
                    Log.d("SearchRepository", "No matching question");
                    networkState.postValue(StringConstants.NO_MATCHING_RESULT);
                }
            }

            @Override
            public void onFailure(@NotNull Call<QuestionsResponse> call, @NotNull Throwable t) {
                t.printStackTrace();
                networkState.postValue(StringConstants.FAILED);
            }
        });
    }

    public LiveData<List<Question>> getQuestions(String inTitle) {
        getQuestionsWithTextInTitle(inTitle);
        return mQuestions;
    }

    public LiveData<String> getNetworkState() {
        return networkState;
    }
//        disposable.add(observable
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(this::handleResults, this::handleError));

//    private void handleResults(List<QuestionsResponse> questionsResponses) {
//        if (questionsResponses != null && questionsResponses.size() != 0) {
//            List<Question> questions = new ArrayList<>();
//            for (int i = 0; i <= questionsResponses.size(); i++) {
//                questions.add(questionsResponses.get(i).getItems().get(0));
//            }
//            mQuestions.setValue(questions);
//        } else {
//            Log.d("SearchRepository", "No matching question");
//        }
//    }

//    private void handleError(Throwable t) {
//        t.printStackTrace();
//    }

//    public void clearDisposable() {
//        disposable.clear();
//    }
}
