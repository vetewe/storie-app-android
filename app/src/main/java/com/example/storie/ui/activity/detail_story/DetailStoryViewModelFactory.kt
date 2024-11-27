package com.example.storie.ui.activity.detail_story

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storie.data.repositories.DetailStoryRepository
import com.example.storie.di.Injection

class DetailStoryViewModelFactory(
    private val detailStoryRepository: DetailStoryRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailStoryViewModel::class.java)) {
            return DetailStoryViewModel(detailStoryRepository) as T
        }
        throw IllegalArgumentException("viewmodel class not found" + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: DetailStoryViewModelFactory? = null

        fun getInstance(
            context: Context
        ): DetailStoryViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: DetailStoryViewModelFactory(
                    Injection.storyDetailRepository(context)
                )
            }.also { instance = it }
    }
}