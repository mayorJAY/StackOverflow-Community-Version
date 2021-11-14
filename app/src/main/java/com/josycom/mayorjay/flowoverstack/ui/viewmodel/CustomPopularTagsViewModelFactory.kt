package com.josycom.mayorjay.flowoverstack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.josycom.mayorjay.flowoverstack.data.TagRepository
import javax.inject.Inject

class CustomPopularTagsViewModelFactory @Inject constructor(private val tagRepository: TagRepository) : ViewModelProvider.Factory {

    private var page = 0
    private var pageSize = 0
    private var inName: String = ""
    private var siteKey: String = ""

    fun setInputs(page: Int, pageSize: Int, inName: String, siteKey: String) {
        this.page = page
        this.pageSize = pageSize
        this.inName = inName
        this.siteKey = siteKey
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PopularTagsDialogViewModel(tagRepository, page, pageSize, inName, siteKey) as T
    }
}