package com.example.storie.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storie.data.response.ListStoryItem
import com.example.storie.data.retrofit.ApiService

class StoryPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getStories()
            val story = response.listStory?.filterNotNull() ?: emptyList()


            LoadResult.Page(
                data = story,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (story.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }


}