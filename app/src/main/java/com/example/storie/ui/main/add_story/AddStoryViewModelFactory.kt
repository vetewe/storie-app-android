package com.example.storie.ui.main.add_story

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storie.data.repositories.AddStoryRepository
import com.example.storie.di.Injection

class AddStoryViewModelFactory(
    private val addStoryRepository: AddStoryRepository
) : ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(addStoryRepository) as T
        } else {
            throw IllegalArgumentException("viewmodel class not found" + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: AddStoryViewModelFactory? = null

        fun getInstance(context: Context): AddStoryViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: AddStoryViewModelFactory(
                    Injection.addStoryRepository(context)
                )
            }.also { instance = it }
    }
}