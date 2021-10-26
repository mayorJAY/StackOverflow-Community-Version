package com.josycom.mayorjay.flowoverstack.model

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.josycom.mayorjay.flowoverstack.network.ApiService

class QuestionDataSourceFactory(private val page: Int, private val pageSize: Int, private val order: String,
                                private val sortCondition: String, private val site: String, private val filter: String,
                                private val siteKey: String, private val apiService: ApiService) : DataSource.Factory<Int, Question>() {

    override fun create(): DataSource<Int, Question> {
        val questionDataSource = QuestionDataSource(page, pageSize, order, sortCondition, site, filter, siteKey, apiService)
        questionLiveDataSource.postValue(questionDataSource)
        return questionDataSource
    }

    companion object {
        val questionLiveDataSource = MutableLiveData<QuestionDataSource>()
    }
}