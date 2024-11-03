package com.josycom.mayorjay.flowoverstack.viewmodel

import androidx.lifecycle.ViewModel
import com.josycom.mayorjay.flowoverstack.data.remote.model.SearchResponse
import com.josycom.mayorjay.flowoverstack.data.repository.SearchRepository
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: SearchRepository) : ViewModel() {

    val responseLiveData = searchRepository.searchResponse

    init {
        responseLiveData.value = SearchResponse()
    }

    fun setQuery(query: String) {
        searchRepository.performSearch(query, AppConstants.FIRST_PAGE, AppConstants.SEARCH_PAGE_SIZE)
    }
}