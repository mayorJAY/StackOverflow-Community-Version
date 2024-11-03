package com.josycom.mayorjay.flowoverstack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.josycom.mayorjay.flowoverstack.data.model.Question
import com.josycom.mayorjay.flowoverstack.data.repository.QuestionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(private val questionRepository: QuestionRepository) : ViewModel() {

    var questionDataFlow: Flow<PagingData<Question>>? = null

    fun init(page: Int, pageSize: Int, order: String, sortCondition: String, site: String, tagged: String, filter: String, siteKey: String) {
        questionRepository.init(page, pageSize, order, sortCondition, site, tagged, filter, siteKey)
        questionDataFlow = questionRepository.questionDataFlow?.cachedIn(viewModelScope)
    }
}