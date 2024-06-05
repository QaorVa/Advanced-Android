package com.example.storiesw.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storiesw.data.model.Story
import com.example.storiesw.data.retrofit.ApiService

class StoryPagingSource(private val apiService : ApiService) : PagingSource<Int, Story>() {
    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getAllStoriesPaging(position, params.loadSize)
            LoadResult.Page(
                data = responseData.data,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.data.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}