package com.josycom.mayorjay.flowoverstack.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.josycom.mayorjay.flowoverstack.model.SearchResponse;
import com.josycom.mayorjay.flowoverstack.repository.SearchRepository;

import javax.inject.Inject;


public class SearchViewModel extends ViewModel {

    private SearchRepository mSearchRepository;
    private MutableLiveData<String> mSearchLiveData = new MutableLiveData<>();
    private LiveData<SearchResponse> mResponseLiveData = Transformations.switchMap(mSearchLiveData, (query) -> mSearchRepository.getResponse(query));

    @Inject
    public SearchViewModel(SearchRepository searchRepository) {
        this.mSearchRepository = searchRepository;
    }

    public LiveData<SearchResponse> getResponseLiveData() {
        return mResponseLiveData;
    }

    public void setQuery(String query) {
        mSearchLiveData.setValue(query);
    }
}
