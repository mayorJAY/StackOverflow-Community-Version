package com.example.josycom.flowoverstack.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.model.QuestionsResponse;
import com.example.josycom.flowoverstack.network.ApiService;
import com.example.josycom.flowoverstack.network.RestApiClient;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchRepository {

    private String inTitle;
    private MutableLiveData<List<Question>> mQuestions = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();
    //private Boolean shouldShowData = true;

    public SearchRepository(String inTitle){
        this.inTitle = inTitle;
        getQuestionsWithTextInTitle();
    }

    public SearchRepository(){
    }

    private void getQuestionsWithTextInTitle() {
        ApiService apiService = RestApiClient.getApiService(ApiService.class);
        Observable<List<QuestionsResponse>> observable = apiService.getQuestionsWithTextInTitle(inTitle);
        disposables.add(observable
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(this::handleResults, this::handleError));
//        call.enqueue(new Callback<QuestionsResponse>() {
//            @Override
//            public void onResponse(Call<QuestionsResponse> call, Response<QuestionsResponse> response) {
//                QuestionsResponse questionsResponse = response.body();
//                if (questionsResponse != null) {
//                    mQuestions.setValue(questionsResponse.getItems());
//                    shouldShowData = true;
//                } else {
//                    Log.d("SearchRepository", "No matching question");
//                    shouldShowData = false;
//                }
//            }
//
//            @Override
//            public void onFailure(Call<QuestionsResponse> call, Throwable t) {
//                shouldShowData = false;
//                t.printStackTrace();
//            }
//        });
    }

    public LiveData<List<Question>> getQuestions() {
        return mQuestions;
    }

    private void handleResults(List<QuestionsResponse> questionsResponses) {
        if (questionsResponses != null && questionsResponses.size() != 0) {
            mQuestions.setValue(questionsResponses.get(0).getItems());
        } else {
            Log.d("SearchRepository", "No matching question");
        }
    }

    private void handleError(Throwable t) {
        t.printStackTrace();
    }

//    public Boolean getShouldShowData() {
//        return shouldShowData;
//    }

    public void clearDisposable(){
        disposables.clear();
    }
}
