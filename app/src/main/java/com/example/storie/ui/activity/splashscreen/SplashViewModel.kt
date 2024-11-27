package com.example.storie.ui.activity.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storie.data.preferences.UserPreference
import kotlinx.coroutines.launch

class SplashViewModel(private val userPreference: UserPreference) : ViewModel() {
    private val _isUserTokenAvailable = MutableLiveData<Boolean>()
    val isUserTokenAvailable: LiveData<Boolean>
        get() = _isUserTokenAvailable

    init {
        checkUserToken()
    }

    private fun checkUserToken() {
        viewModelScope.launch {
            val token = userPreference.getUserToken()
            _isUserTokenAvailable.value = token.isNotEmpty()
        }
    }
}