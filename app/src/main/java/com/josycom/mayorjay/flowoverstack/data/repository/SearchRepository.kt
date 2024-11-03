package com.josycom.mayorjay.flowoverstack.data.repository

import androidx.lifecycle.MutableLiveData
import com.josycom.mayorjay.flowoverstack.data.mapper.toQuestion
import com.josycom.mayorjay.flowoverstack.data.remote.model.QuestionsResponse
import com.josycom.mayorjay.flowoverstack.data.remote.model.SearchResponse
import com.josycom.mayorjay.flowoverstack.data.remote.service.ApiService
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import com.josycom.mayorjay.flowoverstack.util.ThreadExecutor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(private val apiService: ApiService) {

    val searchResponse = MutableLiveData<SearchResponse>()

    private fun getQuestionsWithTextInTitle(inTitle: String?, page: Int, pageSize: Int) {
        searchResponse.postValue(SearchResponse(AppConstants.LOADING, null))
        val call = apiService.getQuestionsWithTextInTitle(inTitle, page, pageSize)
        call.enqueue(object : Callback<QuestionsResponse?> {
            override fun onResponse(call: Call<QuestionsResponse?>, response: Response<QuestionsResponse?>) {
                val questionsResponse = response.body()
                if (questionsResponse != null) {
                    if (questionsResponse.items.isNotEmpty()) {
                        searchResponse.setValue(
                            SearchResponse(
                                AppConstants.LOADED,
                                questionsResponse.items.map { it.toQuestion() })
                        )
                    } else {
                        searchResponse.setValue(SearchResponse(AppConstants.NO_MATCHING_RESULT, null))
                    }
                }
            }

            override fun onFailure(call: Call<QuestionsResponse?>, t: Throwable) {
                Timber.e(t)
                searchResponse.value = SearchResponse(AppConstants.FAILED, null)
            }
        })
    }

    fun performSearch(inTitle: String?, page: Int, pageSize: Int) {
        ThreadExecutor.mExecutor.execute { getQuestionsWithTextInTitle(inTitle, page, pageSize) }
    }
}