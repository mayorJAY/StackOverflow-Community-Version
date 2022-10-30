package com.josycom.mayorjay.flowoverstack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.josycom.mayorjay.flowoverstack.data.repository.SearchRepository
import javax.inject.Inject

class CustomSearchViewModelFactory @Inject constructor(private val searchRepository: SearchRepository) : ViewModelProvider.Factory {

    private var page = 0
    private var pageSize = 0

    fun setInputs(page: Int, pageSize: Int) {
        this.page = page
        this.pageSize = pageSize
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> return SearchViewModel(
                page,
                pageSize,
                searchRepository
            ) as T
            else -> throw IllegalArgumentException("Wrong Class Provided $modelClass")
        }
    }
}