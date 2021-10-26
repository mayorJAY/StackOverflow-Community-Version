package com.josycom.mayorjay.flowoverstack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.josycom.mayorjay.flowoverstack.repository.SearchRepository
import javax.inject.Inject

class CustomSearchViewModelFactory @Inject constructor(private val searchRepository: SearchRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(searchRepository) as T
    }
}