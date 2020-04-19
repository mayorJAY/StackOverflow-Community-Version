package com.example.josycom.flowoverstack.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.model.QuestionsResponse;
import com.example.josycom.flowoverstack.network.ApiService;
import com.example.josycom.flowoverstack.network.RestApiClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {

    private String inTitle;
    private MutableLiveData<List<Question>> mQuestions = new MutableLiveData<>();
    //private final CompositeDisposable disposable = new CompositeDisposable();
    //private Boolean shouldShowData = true;

    public SearchRepository(String inTitle) {
        this.inTitle = inTitle;
        getQuestionsWithTextInTitle();
    }

    public SearchRepository(){
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
                    //shouldShowData = true;
                } else {
                    Log.d("SearchRepository", "No matching question");
                    //shouldShowData = false;
                }
            }

            @Override
            public void onFailure(Call<QuestionsResponse> call, Throwable t) {
                //shouldShowData = false;
                t.printStackTrace();
            }
        });
    }

    public LiveData<List<Question>> getQuestions() {
        return mQuestions;
    }

//        disposable.add(observable
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(this::handleResults, this::handleError));

//    private void handleResults(List<QuestionsResponse> questionsResponses) {
//        if (questionsResponses != null && questionsResponses.size() != 0) {
//            List<Question> questions = new ArrayList<>();
//            for (int i = 0; i <= questionsResponses.size(); i++){
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

//    public Boolean getShouldShowData() {
//        return shouldShowData;
//    }

//    public void clearDisposable() {
//        disposable.clear();
//    }
}
