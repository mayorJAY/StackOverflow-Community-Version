package com.josycom.mayorjay.flowoverstack.viewmodel

import com.josycom.mayorjay.flowoverstack.data.repository.AnswerRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.josycom.mayorjay.flowoverstack.data.model.Answer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AnswerViewModel @Inject constructor(private val answerRepository: AnswerRepository) : ViewModel() {

    var answerDataFlow: Flow<PagingData<Answer>>? = null

    fun init(questionId: Int, order: String, sortCondition: String, site: String, filter: String, siteKey: String) {
        answerRepository.init(questionId, order, sortCondition, site, filter, siteKey)
        answerDataFlow = answerRepository.answerDataFlow?.cachedIn(viewModelScope)
    }
}