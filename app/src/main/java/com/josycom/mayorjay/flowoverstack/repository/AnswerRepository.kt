package com.josycom.mayorjay.flowoverstack.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.josycom.mayorjay.flowoverstack.model.Answer
import com.josycom.mayorjay.flowoverstack.model.AnswerResponse
import com.josycom.mayorjay.flowoverstack.network.ApiService
import com.josycom.mayorjay.flowoverstack.util.ThreadExecutor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnswerRepository @Inject constructor(private val apiService: ApiService) {

    private var questionId = 0
    private var order: String? = null
    private var sortCondition: String? = null
    private var site: String? = null
    private var filter: String? = null
    private var siteKey: String? = null
    private val mAnswers = MutableLiveData<List<Answer>>()
    val answers: LiveData<List<Answer>>
        get() = mAnswers

    fun init(questionId: Int, order: String?, sortCondition: String?, site: String?, filter: String?, siteKey: String?) {
        this.questionId = questionId
        this.order = order
        this.sortCondition = sortCondition
        this.site = site
        this.filter = filter
        this.siteKey = siteKey
        getAnswersToQuestion()
    }

    private fun getAnswersToQuestion() {
            ThreadExecutor.mExecutor.execute {
                val call = apiService.getAnswersToQuestion(questionId, order, sortCondition, site, filter, siteKey)
                call.enqueue(object : Callback<AnswerResponse?> {
                    override fun onResponse(call: Call<AnswerResponse?>, response: Response<AnswerResponse?>) {
                        val answerResponse = response.body()
                        if (answerResponse != null) {
                            mAnswers.value = answerResponse.items
                        }
                    }

                    override fun onFailure(call: Call<AnswerResponse?>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }
    }
}