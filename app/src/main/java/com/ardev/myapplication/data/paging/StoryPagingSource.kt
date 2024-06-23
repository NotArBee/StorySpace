package com.ardev.myapplication.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ardev.myapplication.data.response.ListStoryItem
import com.ardev.myapplication.data.retrofit.ApiService

class StoryPagingSource(private val apiService: ApiService, private val token: String) : PagingSource<Int, ListStoryItem>() {

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getStories("Bearer $token", position, params.loadSize)

            if (response.isSuccessful) {
                val stories = response.body()?.listStory ?: emptyList()
                LoadResult.Page(
                    data = stories,
                    prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                    nextKey = if (stories.isEmpty()) null else position + 1
                )
            } else {
                LoadResult.Error(Exception("Failed to load data!"))
            }
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}
