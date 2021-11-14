package com.josycom.mayorjay.flowoverstack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.josycom.mayorjay.flowoverstack.data.TagRepository
import com.josycom.mayorjay.flowoverstack.model.Tag
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PopularTagsDialogViewModel @Inject constructor(tagRepository: TagRepository, page: Int, pageSize: Int, inName: String, siteKey: String) : ViewModel() {

    var pagingDataFlow: Flow<PagingData<Tag>>? = null

    init {
        tagRepository.init(page, pageSize, inName, siteKey)
        pagingDataFlow = tagRepository.tagResult?.cachedIn(viewModelScope)
    }
}