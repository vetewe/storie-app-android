package com.example.storie.ui.main.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storie.data.preferences.UserPreference
import com.example.storie.di.Injection

class ProfileViewModelFactory(
    private val userPreference: UserPreference
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(userPreference) as T
        }
        throw IllegalArgumentException("viewmodel class not found" + modelClass.name)
    }


    companion object {
        @Volatile
        private var instance: ProfileViewModelFactory? = null

        fun getInstance(context: Context)
                : ProfileViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ProfileViewModelFactory(
                    Injection.userPreference(context)
                )
            }.also { instance = it }
    }
}