package com.josycom.mayorjay.flowoverstack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.josycom.mayorjay.flowoverstack.data.SearchRepository
import javax.inject.Inject

class CustomSearchViewModelFactory @Inject constructor(private val searchRepository: SearchRepository) : ViewModelProvider.Factory {

    private var page = 0
    private var pageSize = 0

    fun setInputs(page: Int, pageSize: Int) {
        this.page = page
        this.pageSize = pageSize
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(page, pageSize, searchRepository) as T
    }
}