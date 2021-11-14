package com.josycom.mayorjay.flowoverstack.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.josycom.mayorjay.flowoverstack.data.AnswerRepository
import javax.inject.Inject

class CustomAnswerViewModelFactory @Inject constructor(private val answerRepository: AnswerRepository) : ViewModelProvider.Factory {

    private var order: String? = null
    private var sortCondition: String? = null
    private var site: String? = null
    private var filter: String? = null
    private var questionId = 0
    private var siteKey: String? = null

    fun setInputs(questionId: Int, order: String?, sortCondition: String?, site: String?, filter: String?, siteKey: String?) {
        this.questionId = questionId
        this.order = order
        this.sortCondition = sortCondition
        this.site = site
        this.filter = filter
        this.siteKey = siteKey
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AnswerViewModel(answerRepository, questionId, order, sortCondition, site, filter, siteKey) as T
    }
}