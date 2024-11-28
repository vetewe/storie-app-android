package com.example.storie.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storie.data.preferences.UserPreference
import com.example.storie.data.repositories.StoryRepository
import com.example.storie.data.response.ListStoryItem
import kotlinx.coroutines.launch

class HomeViewModel(
    storyRepository: StoryRepository,
    private val userPreference: UserPreference
) : ViewModel() {

    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName


    init {
        getUserData()
    }

    private fun getUserData() {
        viewModelScope.launch {
            _userName.value = userPreference.getUserName()
        }
    }


}