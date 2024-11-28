package com.example.storie.data.repositories

import com.example.storie.data.response.AddStoryResponse
import com.example.storie.data.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryRepository(private val apiService: ApiService) {
    suspend fun addStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): AddStoryResponse {
        return apiService.addStories(file, description, lat, lon)
    }


    companion object {
        @Volatile
        private var instance: AddStoryRepository? = null

        fun getInstance(
            apiService: ApiService
        ): AddStoryRepository =
            instance ?: synchronized(this) {
                instance ?: AddStoryRepository(apiService)
            }.also { instance = it }
    }
}