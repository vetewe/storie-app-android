package com.example.storie.ui.activity.maps

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storie.data.repositories.StoryLocationRepository
import com.example.storie.di.Injection

class MapsViewModelFactory private constructor(
    private val storyLocationRepository: StoryLocationRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(storyLocationRepository) as T
        } else {
            throw IllegalArgumentException("Unknown viewmodel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: MapsViewModelFactory? = null
        fun getInstance(
            context: Context
        ): MapsViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: MapsViewModelFactory(
                    Injection.storyLocationRepository(context)
                )
            }.also { instance = it }
    }
}