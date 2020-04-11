package com.example.josycom.flowoverstack.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.repository.SearchRepository;

import java.util.List;

public class SearchViewModel extends ViewModel {

    private LiveData<List<Question>> mQuestionLiveData;

    SearchViewModel(String inTitle) {
        SearchRepository searchRepository = new SearchRepository(inTitle);
        mQuestionLiveData = searchRepository.getQuestions();
    }

    public LiveData<List<Question>> getQuestionLiveData() {
        return mQuestionLiveData;
    }
}
