package com.josycom.mayorjay.flowoverstack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.josycom.mayorjay.flowoverstack.data.TagRepository
import com.josycom.mayorjay.flowoverstack.model.Tag
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TagsDialogViewModel @Inject constructor(private val tagRepository: TagRepository, val page: Int, val pageSize: Int, val siteKey: String) : ViewModel() {

    var tagDataFlow: Flow<PagingData<Tag>>? = null

    fun fetchTags(inName: String) {
        tagRepository.init(page, pageSize, inName, siteKey)
        tagDataFlow = tagRepository.tagDataFlow?.cachedIn(viewModelScope)
    }
}