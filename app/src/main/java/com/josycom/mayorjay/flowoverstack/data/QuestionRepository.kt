package com.josycom.mayorjay.flowoverstack.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.josycom.mayorjay.flowoverstack.model.Question
import com.josycom.mayorjay.flowoverstack.network.ApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepository @Inject constructor(private val apiService: ApiService) {

    var questionDataFlow: Flow<PagingData<Question>>? = null

    fun init(page: Int, pageSize: Int, order: String, sortCondition: String, site: String, tagged: String, filter: String, siteKey: String) {
        questionDataFlow = Pager(PagingConfig(pageSize, enablePlaceholders = false),
                pagingSourceFactory = { QuestionPagingSource(page, pageSize, order, sortCondition, site, tagged, filter, siteKey, apiService) }).flow
    }
}