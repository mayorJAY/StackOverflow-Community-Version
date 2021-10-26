package com.josycom.mayorjay.flowoverstack.model

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.josycom.mayorjay.flowoverstack.network.ApiService
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionDataSource internal constructor(private val page: Int, private val pageSize: Int, private val order: String, private val sortCondition: String,
                                              private val site: String, private val filter: String, private val siteKey: String,
                                              private val apiService: ApiService) : PageKeyedDataSource<Int, Question>() {

    val networkState: MutableLiveData<String> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Question?>) {
        networkState.postValue(AppConstants.LOADING)
        val call = apiService.getQuestionsForAll(page, pageSize, order, sortCondition, site, filter, siteKey)
        call.enqueue(object : Callback<QuestionsResponse?> {
            override fun onResponse(call: Call<QuestionsResponse?>, response: Response<QuestionsResponse?>) {
                val apiResponse = response.body()
                if (apiResponse != null) {
                    networkState.value = AppConstants.LOADED
                    val responseItems: List<Question?>? = apiResponse.items
                    callback.onResult(responseItems!!, null, page + 1)
                } else {
                    networkState.setValue(AppConstants.FAILED)
                }
            }

            override fun onFailure(call: Call<QuestionsResponse?>, t: Throwable) {
                t.printStackTrace()
                networkState.value = AppConstants.FAILED
            }
        })
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Question?>) {
        val call = apiService.getQuestionsForAll(params.key, pageSize, order, sortCondition, site, filter, siteKey)
        call.enqueue(object : Callback<QuestionsResponse?> {
            override fun onResponse(call: Call<QuestionsResponse?>, response: Response<QuestionsResponse?>) {
                val apiResponse = response.body()
                if (apiResponse != null) {
                    networkState.value = AppConstants.LOADED
                    val responseItems: List<Question?>? = apiResponse.items
                    val key: Int = if (params.key > 1) params.key - 1 else 0
                    callback.onResult(responseItems!!, key)
                } else {
                    networkState.setValue(AppConstants.FAILED)
                }
            }

            override fun onFailure(call: Call<QuestionsResponse?>, t: Throwable) {
                t.printStackTrace()
                networkState.value = AppConstants.FAILED
            }
        })
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Question?>) {
        val call = apiService.getQuestionsForAll(params.key, pageSize, order, sortCondition, site, filter, siteKey)
        call.enqueue(object : Callback<QuestionsResponse?> {
            override fun onResponse(call: Call<QuestionsResponse?>, response: Response<QuestionsResponse?>) {
                val apiResponse = response.body()
                if (apiResponse != null) {
                    networkState.value = AppConstants.LOADED
                    val responseItems: List<Question?>? = apiResponse.items
                    callback.onResult(responseItems!!, params.key + 1)
                } else {
                    networkState.setValue(AppConstants.FAILED)
                }
            }

            override fun onFailure(call: Call<QuestionsResponse?>, t: Throwable) {
                t.printStackTrace()
                networkState.value = AppConstants.FAILED
            }
        })
    }

}