package com.josycom.mayorjay.flowoverstack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.josycom.mayorjay.flowoverstack.data.repository.TagRepository
import com.josycom.mayorjay.flowoverstack.data.model.Tag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TagsDialogViewModel @Inject constructor(private val tagRepository: TagRepository) : ViewModel() {

    var tagDataFlow: Flow<PagingData<Tag>>? = null

    fun fetchTags(inName: String, page: Int, pageSize: Int, siteKey: String) {
        tagRepository.init(page, pageSize, inName, siteKey)
        tagDataFlow = tagRepository.tagDataFlow?.cachedIn(viewModelScope)
    }
}