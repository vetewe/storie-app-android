package com.example.storie.ui.activity.login

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storie.data.preferences.UserPreference
import com.example.storie.data.repositories.LoginRepository
import com.example.storie.di.Injection

class LoginViewModelFactory private constructor(
    private val application: Application,
    private val loginRepository: LoginRepository,
    private val userPreference: UserPreference
) : ViewModelProvider.AndroidViewModelFactory(application) {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(application, loginRepository, userPreference) as T
        }
        throw IllegalArgumentException("viewmodel class not found" + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: LoginViewModelFactory? = null
        fun getInstance(
            application: Application,
            userPreference: UserPreference
        ): LoginViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: LoginViewModelFactory(
                    application,
                    Injection.loginRepository(application),
                    userPreference
                )
            }.also { instance = it }
    }
}