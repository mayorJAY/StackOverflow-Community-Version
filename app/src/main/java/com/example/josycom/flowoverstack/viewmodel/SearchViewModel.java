package com.example.josycom.flowoverstack.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.josycom.flowoverstack.model.Question;
import com.example.josycom.flowoverstack.repository.SearchRepository;

import java.util.List;

public class SearchViewModel extends ViewModel {

    private SearchRepository mSearchRepository;
    private MutableLiveData<String> mSearchLiveData = new MutableLiveData<>();
    private LiveData<List<Question>> mQuestionLiveData = Transformations.switchMap(mSearchLiveData, (query) -> mSearchRepository.getQuestions(query));

    public SearchViewModel() {
        mSearchRepository = new SearchRepository();
    }

    public LiveData<List<Question>> getQuestionLiveData() {
        return mQuestionLiveData;
    }

    public void setQuery(String query) {
        mSearchLiveData.setValue(query);
    }
}
