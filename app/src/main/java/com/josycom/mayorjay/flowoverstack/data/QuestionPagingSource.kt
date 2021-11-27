package com.josycom.mayorjay.flowoverstack.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.josycom.mayorjay.flowoverstack.model.Question
import com.josycom.mayorjay.flowoverstack.network.ApiService
import retrofit2.HttpException
import java.io.IOException

class QuestionPagingSource(private val page: Int, private val pageSize: Int, private val order: String, private val sortCondition: String,
                                                private val site: String, private val tagged: String, private val filter: String, private val siteKey: String,
                                                private val apiService: ApiService) : PagingSource<Int, Question>() {

    override fun getRefreshKey(state: PagingState<Int, Question>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Question> {
        val position = params.key ?: page
        return try {
            val response = apiService.getQuestionsForAll(position, pageSize, order, sortCondition, site, tagged, filter, siteKey)
            val responseItems: List<Question> = response.items
            val nextKey = if (responseItems.isEmpty()) {
                null
            } else {
                position + (params.loadSize / pageSize)
            }
            LoadResult.Page(
                    data = responseItems,
                    prevKey = if (position == page) null else position - 1,
                    nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}