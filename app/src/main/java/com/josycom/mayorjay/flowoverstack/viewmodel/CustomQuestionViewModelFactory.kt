package com.josycom.mayorjay.flowoverstack.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.josycom.mayorjay.flowoverstack.data.repository.QuestionRepository
import javax.inject.Inject

class CustomQuestionViewModelFactory @Inject constructor(private val questionRepository: QuestionRepository) : ViewModelProvider.Factory {

    private var page = 0
    private var pageSize = 0
    private var order: String = ""
    private var sortCondition: String = ""
    private var site: String = ""
    private var tagged: String = ""
    private var filter: String = ""
    private var siteKey: String = ""

    fun setInputs(page: Int, pageSize: Int, order: String, sortCondition: String, site: String, tagged: String, filter: String, siteKey: String) {
        this.page = page
        this.pageSize = pageSize
        this.order = order
        this.sortCondition = sortCondition
        this.site = site
        this.tagged = tagged
        this.filter = filter
        this.siteKey = siteKey
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(QuestionViewModel::class.java) -> return QuestionViewModel(
                questionRepository, page, pageSize,
                order, sortCondition, site, tagged, filter, siteKey
            ) as T
            else -> throw IllegalArgumentException("Wrong Class Provided $modelClass")
        }
    }
}