package com.example.storie.di

import android.content.Context
import com.example.storie.data.preferences.UserPreference
import com.example.storie.data.preferences.dataStore
import com.example.storie.data.repositories.*
import com.example.storie.data.retrofit.ApiConfig
import kotlinx.coroutines.runBlocking

object Injection {

    fun storyRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return StoryRepository.getInstance(apiService)
    }

    fun storyDetailRepository(context: Context): DetailStoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return DetailStoryRepository.getInstance(apiService)
    }

    fun widgetStoryRepository(context: Context): WidgetStoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return WidgetStoryRepository.getInstance(apiService)
    }


    fun addStoryRepository(context: Context): AddStoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return AddStoryRepository.getInstance(apiService)
    }

    fun userPreference(context: Context): UserPreference {
        return UserPreference.getInstance(context.dataStore)
    }

    fun registerRepository(context: Context): RegisterRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return RegisterRepository.getInstance(apiService)
    }

    fun loginRepository(context: Context): LoginRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getUserToken() }
        val apiService = ApiConfig.getApiService(user)
        return LoginRepository.getInstance(apiService)
    }
}