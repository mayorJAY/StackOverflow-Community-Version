package com.josycom.mayorjay.flowoverstack.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.josycom.mayorjay.flowoverstack.model.Answer
import com.josycom.mayorjay.flowoverstack.network.ApiService
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnswerRepository @Inject constructor(private val apiService: ApiService) {

    var answerDataFlow: Flow<PagingData<Answer>>? = null

    fun init(questionId: Int, order: String, sortCondition: String, site: String, filter: String, siteKey: String) {
        answerDataFlow = Pager(PagingConfig(AppConstants.PAGE_SIZE, enablePlaceholders = false),
                pagingSourceFactory = {AnswerPagingSource(questionId, order, sortCondition, site, filter, siteKey, apiService)}).flow
    }
}