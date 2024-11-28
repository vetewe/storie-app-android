package com.example.storie.ui.activity.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.storie.R
import com.example.storie.data.preferences.UserPreference
import com.example.storie.data.repositories.LoginRepository
import com.example.storie.data.response.LoginResponse
import com.example.storie.utils.IdlingResource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(
    application: Application,
    private val loginRepository: LoginRepository,
    private val userPreference: UserPreference
) : AndroidViewModel(application) {

    private val _showSuccessDialog = MutableLiveData<String>()
    val showSuccessDialog: LiveData<String>
        get() = _showSuccessDialog

    private val _showErrorDialog = MutableLiveData<String>()
    val showErrorDialog: LiveData<String>
        get() = _showErrorDialog

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    fun loginUser(email: String, password: String) {
        _loading.value = true
        IdlingResource.increment()

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = loginRepository.login(email, password)
                val loginResult = response.loginResult
                val userId = loginResult?.userId
                val username = loginResult?.name
                val userToken = loginResult?.token
                _loading.postValue(false)
                _showSuccessDialog.postValue(getApplication<Application>().getString(R.string.login_succes_dialog))
                userToken?.let { token ->
                    userId?.let { id ->
                        username?.let { name ->
                            saveUserData(id, name, token)
                        }
                    }
                }
            } catch (e: HttpException) {
                _loading.postValue(false)
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message
                    ?: getApplication<Application>().getString(R.string.login_error_messege)
                _showErrorDialog.postValue(errorMessage)
            } catch (e: Exception) {
                _loading.postValue(false)
                _showErrorDialog.postValue(
                    getApplication<Application>().getString(R.string.login_failed_dialog)
                )
            } finally {
                IdlingResource.decrement()
            }
        }
    }

    private fun saveUserData(userId: String, username: String, userToken: String) {
        viewModelScope.launch {
            userPreference.saveUserData(token = userToken, id = userId, name = username)
        }
    }
}