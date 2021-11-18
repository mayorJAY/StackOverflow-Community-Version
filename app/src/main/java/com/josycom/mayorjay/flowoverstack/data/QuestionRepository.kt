package com.josycom.mayorjay.flowoverstack.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.josycom.mayorjay.flowoverstack.model.Question
import com.josycom.mayorjay.flowoverstack.network.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestionRepository @Inject constructor(private val apiService: ApiService) {

    var questionPagedList: LiveData<PagedList<Question>>? = null

    fun init(page: Int, pageSize: Int, order: String, sortCondition: String, site: String, tagged: String, filter: String, siteKey: String) {
        val factory = QuestionDataSourceFactory(page, pageSize, order, sortCondition, site, tagged, filter, siteKey, apiService)
        val pageConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(pageSize)
                .build()
        questionPagedList = LivePagedListBuilder(factory, pageConfig).build()
    }
}