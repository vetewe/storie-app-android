package com.example.storie.ui.activity.detail_story

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storie.data.repositories.DetailStoryRepository

class DetailStoryViewModelFactory(
    private val application: Application,
    private val detailStoryRepository: DetailStoryRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailStoryViewModel::class.java)) {
            return DetailStoryViewModel(application, detailStoryRepository) as T
        }
        throw IllegalArgumentException("viewmodel class not found" + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: DetailStoryViewModelFactory? = null

        fun getInstance(
            application: Application,
            detailStoryRepository: DetailStoryRepository
        ): DetailStoryViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: DetailStoryViewModelFactory(
                    application, detailStoryRepository
                )
            }.also { instance = it }
    }
}