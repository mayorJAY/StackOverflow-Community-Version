package com.josycom.mayorjay.flowoverstack.data

import androidx.lifecycle.MutableLiveData
import com.josycom.mayorjay.flowoverstack.network.QuestionsResponse
import com.josycom.mayorjay.flowoverstack.network.SearchResponse
import com.josycom.mayorjay.flowoverstack.network.ApiService
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import com.josycom.mayorjay.flowoverstack.util.ThreadExecutor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(private val apiService: ApiService) {

    private val mResponse = MutableLiveData<SearchResponse>()

    private fun getQuestionsWithTextInTitle(inTitle: String, page: Int, pageSize: Int) {
        mResponse.postValue(SearchResponse(AppConstants.LOADING, null))
        val call = apiService.getQuestionsWithTextInTitle(inTitle, page, pageSize)
        call.enqueue(object : Callback<QuestionsResponse?> {
            override fun onResponse(call: Call<QuestionsResponse?>, response: Response<QuestionsResponse?>) {
                val questionsResponse = response.body()
                if (questionsResponse != null) {
                    if (questionsResponse.items.isNotEmpty()) {
                        mResponse.setValue(SearchResponse(AppConstants.LOADED, questionsResponse.items))
                    } else {
                        mResponse.setValue(SearchResponse(AppConstants.NO_MATCHING_RESULT, null))
                    }
                }
            }

            override fun onFailure(call: Call<QuestionsResponse?>, t: Throwable) {
                t.printStackTrace()
                mResponse.value = SearchResponse(AppConstants.FAILED, null)
            }
        })
    }

    fun getResponse(inTitle: String, page: Int, pageSize: Int): MutableLiveData<SearchResponse> {
        ThreadExecutor.mExecutor.execute { getQuestionsWithTextInTitle(inTitle, page, pageSize) }
        return mResponse
    }
}