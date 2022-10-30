package com.josycom.mayorjay.flowoverstack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.josycom.mayorjay.flowoverstack.data.repository.TagRepository
import javax.inject.Inject

class CustomTagsViewModelFactory @Inject constructor(private val tagRepository: TagRepository) : ViewModelProvider.Factory {

    private var page = 0
    private var pageSize = 0
    private var siteKey: String = ""

    fun setInputs(page: Int, pageSize: Int, siteKey: String) {
        this.page = page
        this.pageSize = pageSize
        this.siteKey = siteKey
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(TagsDialogViewModel::class.java) -> return TagsDialogViewModel(
                tagRepository,
                page,
                pageSize,
                siteKey
            ) as T
            else -> throw IllegalArgumentException("Wrong Class Provided $modelClass")
        }
    }
}