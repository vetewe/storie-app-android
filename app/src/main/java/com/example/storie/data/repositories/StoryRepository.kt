package com.example.storie.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storie.data.database.StoryDatabase
import com.example.storie.data.database.StoryRemoteMediator
import com.example.storie.data.response.ListStoryItem
import com.example.storie.data.retrofit.ApiService

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
) {

    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    companion object {

        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(
            storyDatabase: StoryDatabase,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, storyDatabase)
            }.also { instance = it }
    }
}