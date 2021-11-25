package com.josycom.mayorjay.flowoverstack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.josycom.mayorjay.flowoverstack.model.Question
import com.josycom.mayorjay.flowoverstack.data.QuestionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuestionViewModel @Inject constructor(questionRepository: QuestionRepository, page: Int, pageSize: Int, order: String, sortCondition: String, site: String, tagged: String, filter: String, siteKey: String) : ViewModel() {

    val questionDataFlow: Flow<PagingData<Question>>?
//    val networkState: LiveData<String>

    init {
        questionRepository.init(page, pageSize, order, sortCondition, site, tagged, filter, siteKey)
        questionDataFlow = questionRepository.questionDataFlow?.cachedIn(viewModelScope)
//        val liveDataSource: LiveData<QuestionDataSource> = QuestionDataSourceFactory.questionLiveDataSource
//        networkState = Transformations.switchMap(liveDataSource, QuestionDataSource::networkState)
    }

    fun refresh() {
        //QuestionDataSourceFactory.questionLiveDataSource.value!!.invalidate()
    }
}