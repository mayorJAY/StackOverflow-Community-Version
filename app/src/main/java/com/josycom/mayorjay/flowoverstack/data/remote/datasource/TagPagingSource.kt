package com.josycom.mayorjay.flowoverstack.data.remote.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.josycom.mayorjay.flowoverstack.data.mapper.toTag
import com.josycom.mayorjay.flowoverstack.data.model.Tag
import com.josycom.mayorjay.flowoverstack.data.remote.service.ApiService
import retrofit2.HttpException
import java.io.IOException

class TagPagingSource(private val page: Int, private val pageSize: Int, private val inName: String, private val siteKey: String, private val apiService: ApiService) : PagingSource<Int, Tag>() {

    override fun getRefreshKey(state: PagingState<Int, Tag>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Tag> {
        val position = params.key ?: page
        return try {
            val response = apiService.getAllPopularTags(position, pageSize, inName, siteKey)
            val responseItems: List<Tag> = response.items.map { it.toTag() }
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