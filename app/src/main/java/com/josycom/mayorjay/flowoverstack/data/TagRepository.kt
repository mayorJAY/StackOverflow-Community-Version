package com.josycom.mayorjay.flowoverstack.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.josycom.mayorjay.flowoverstack.model.Tag
import com.josycom.mayorjay.flowoverstack.network.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TagRepository @Inject constructor(private val apiService: ApiService) {

    var tagDataFlow: Flow<PagingData<Tag>>? = null

    fun init(page: Int, pageSize: Int, inName: String, siteKey: String) {
        tagDataFlow = Pager(PagingConfig(pageSize, enablePlaceholders = false), pagingSourceFactory = { TagPagingSource(page, pageSize, inName, siteKey, apiService) }).flow
    }
}