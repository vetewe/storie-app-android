package com.example.storie.ui.activity.detail_story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storie.data.repositories.DetailStoryRepository
import com.example.storie.data.response.Story
import com.example.storie.data.Result
import kotlinx.coroutines.launch

class DetailStoryViewModel(
    private val storyDetailStoryRepository: DetailStoryRepository
) : ViewModel() {

    private val _storyDetail = MutableLiveData<Result<Story?>>()
    val storyDetail: LiveData<Result<Story?>>
        get() = _storyDetail

    fun findDetailStory(storyId: String) {
        viewModelScope.launch {
            try {
                val result = storyDetailStoryRepository.getDetailStory(storyId)
                _storyDetail.value = result
            } catch (e: Exception) {
                _storyDetail.value = Result.Error(e.message.toString())
                Log.e(TAG, "Error: ${e.message.toString()}")
            }
        }
    }

    companion object {
        private const val TAG = "DetailStoryViewModel"
    }
}