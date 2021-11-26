package com.josycom.mayorjay.flowoverstack.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.josycom.mayorjay.flowoverstack.model.Answer
import com.josycom.mayorjay.flowoverstack.network.ApiService
import com.josycom.mayorjay.flowoverstack.util.AppConstants
import retrofit2.HttpException
import java.io.IOException

class AnswerPagingSource(private val questionId: Int, private val order: String, private val sortCondition: String, private val site: String,
                         private val filter: String, private val siteKey: String, private val apiService: ApiService) : PagingSource<Int, Answer>() {

    override fun getRefreshKey(state: PagingState<Int, Answer>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Answer> {
        val position = params.key ?: AppConstants.FIRST_PAGE
        return try {
            val response = apiService.getAnswersToQuestion(questionId, order, sortCondition, site, filter, siteKey)
            val responseItems: List<Answer> = response.items
            val nextKey = if (responseItems.isEmpty() || responseItems.size <= AppConstants.PAGE_SIZE) {
                null
            } else {
                position + (params.loadSize / AppConstants.PAGE_SIZE)
            }
            LoadResult.Page(
                    data = responseItems,
                    prevKey = if (position == AppConstants.FIRST_PAGE) null else position - 1,
                    nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}