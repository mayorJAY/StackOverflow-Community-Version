package com.josycom.mayorjay.flowoverstack.ui.viewmodel

import com.josycom.mayorjay.flowoverstack.data.AnswerRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.josycom.mayorjay.flowoverstack.model.Answer
import kotlinx.coroutines.flow.Flow

class AnswerViewModel(answerRepository: AnswerRepository, questionId: Int, order: String, sortCondition: String, site: String, filter: String, siteKey: String) : ViewModel() {

    val answerDataFlow: Flow<PagingData<Answer>>?

    init {
        answerRepository.init(questionId, order, sortCondition, site, filter, siteKey)
        answerDataFlow = answerRepository.answerDataFlow?.cachedIn(viewModelScope)
    }
}