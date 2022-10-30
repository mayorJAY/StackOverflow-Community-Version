package com.josycom.mayorjay.flowoverstack.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.josycom.mayorjay.flowoverstack.data.repository.SearchRepository
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val page: Int, private val pageSize: Int, private val mSearchRepository: SearchRepository) : ViewModel() {

    private val mSearchLiveData = MutableLiveData<String>()
    val responseLiveData = Transformations.switchMap(mSearchLiveData) { query: String? -> mSearchRepository.getResponse(query, page, pageSize) }

    fun setQuery(query: String) {
        mSearchLiveData.value = query
    }
}