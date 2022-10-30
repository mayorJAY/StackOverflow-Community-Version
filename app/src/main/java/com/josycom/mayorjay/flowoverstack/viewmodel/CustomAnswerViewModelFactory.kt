package com.josycom.mayorjay.flowoverstack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.josycom.mayorjay.flowoverstack.data.repository.AnswerRepository
import javax.inject.Inject

class CustomAnswerViewModelFactory @Inject constructor(private val answerRepository: AnswerRepository) : ViewModelProvider.Factory {

    private var order: String = ""
    private var sortCondition: String = ""
    private var site: String = ""
    private var filter: String = ""
    private var questionId = 0
    private var siteKey: String = ""

    fun setInputs(questionId: Int, order: String, sortCondition: String, site: String, filter: String, siteKey: String) {
        this.questionId = questionId
        this.order = order
        this.sortCondition = sortCondition
        this.site = site
        this.filter = filter
        this.siteKey = siteKey
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(AnswerViewModel::class.java) -> return AnswerViewModel(
                answerRepository, questionId, order,
                sortCondition, site, filter, siteKey
            ) as T
            else -> throw IllegalArgumentException("Wrong Class Provided $modelClass")
        }
    }
}