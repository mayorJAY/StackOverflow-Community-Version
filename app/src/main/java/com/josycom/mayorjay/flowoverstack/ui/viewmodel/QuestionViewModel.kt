package com.josycom.mayorjay.flowoverstack.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.josycom.mayorjay.flowoverstack.model.Question
import com.josycom.mayorjay.flowoverstack.data.QuestionDataSource
import com.josycom.mayorjay.flowoverstack.data.QuestionDataSourceFactory
import com.josycom.mayorjay.flowoverstack.data.QuestionRepository
import javax.inject.Inject

class QuestionViewModel @Inject constructor(questionRepository: QuestionRepository, page: Int, pageSize: Int, order: String, sortCondition: String, site: String, tagged: String, filter: String, siteKey: String) : ViewModel() {

    val questionPagedList: LiveData<PagedList<Question>>?
    val networkState: LiveData<String>

    init {
        questionRepository.init(page, pageSize, order, sortCondition, site, tagged, filter, siteKey)
        questionPagedList = questionRepository.questionPagedList
        val liveDataSource: LiveData<QuestionDataSource> = QuestionDataSourceFactory.questionLiveDataSource
        networkState = Transformations.switchMap(liveDataSource, QuestionDataSource::networkState)
    }

    fun refresh() {
        QuestionDataSourceFactory.questionLiveDataSource.value!!.invalidate()
    }
}