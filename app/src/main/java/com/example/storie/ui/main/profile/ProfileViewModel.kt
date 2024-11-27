package com.example.storie.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storie.data.preferences.UserPreference
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val userPreference: UserPreference
) : ViewModel() {

    private val _userId = MutableLiveData<String>()
    val userId: LiveData<String>
        get() = _userId

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    init {
        getUserData()
    }

    private fun getUserData() {
        viewModelScope.launch {
            _userId.value = userPreference.getUserId()
            _userName.value = userPreference.getUserName()
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreference.clearUserData()
        }
    }
}