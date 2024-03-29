package com.josycom.mayorjay.flowoverstack.viewmodel

import com.josycom.mayorjay.flowoverstack.data.repository.AnswerRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.josycom.mayorjay.flowoverstack.data.model.Answer
import kotlinx.coroutines.flow.Flow

class AnswerViewModel(answerRepository: AnswerRepository, questionId: Int, order: String, sortCondition: String, site: String, filter: String, siteKey: String) : ViewModel() {

    val answerDataFlow: Flow<PagingData<Answer>>?

    init {
        answerRepository.init(questionId, order, sortCondition, site, filter, siteKey)
        answerDataFlow = answerRepository.answerDataFlow?.cachedIn(viewModelScope)
    }
}