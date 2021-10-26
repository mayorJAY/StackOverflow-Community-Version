package com.josycom.mayorjay.flowoverstack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.josycom.mayorjay.flowoverstack.repository.QuestionRepository
import javax.inject.Inject

class CustomQuestionViewModelFactory @Inject constructor(private val questionRepository: QuestionRepository) : ViewModelProvider.Factory {

    private var page = 0
    private var pageSize = 0
    private var order: String? = null
    private var sortCondition: String? = null
    private var site: String? = null
    private var filter: String? = null
    private var siteKey: String? = null

    fun setInputs(page: Int, pageSize: Int, order: String?, sortCondition: String?, site: String?, filter: String?, siteKey: String?) {
        this.page = page
        this.pageSize = pageSize
        this.order = order
        this.sortCondition = sortCondition
        this.site = site
        this.filter = filter
        this.siteKey = siteKey
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return QuestionViewModel(questionRepository, page, pageSize, order, sortCondition, site, filter, siteKey) as T
    }
}