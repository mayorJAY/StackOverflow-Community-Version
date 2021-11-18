package com.josycom.mayorjay.flowoverstack.ui.viewmodel

import com.josycom.mayorjay.flowoverstack.data.AnswerRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import com.josycom.mayorjay.flowoverstack.model.Answer

class AnswerViewModel(answerRepository: AnswerRepository, questionId: Int, order: String?, sortCondition: String?, site: String?, filter: String?, siteKey: String?) : ViewModel() {
    val answersLiveData: LiveData<List<Answer>>

    init {
        answerRepository.init(questionId, order, sortCondition, site, filter, siteKey)
        answersLiveData = answerRepository.answers
    }
}