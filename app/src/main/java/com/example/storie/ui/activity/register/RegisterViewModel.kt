package com.example.storie.ui.activity.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.storie.R
import com.example.storie.data.repositories.RegisterRepository
import com.example.storie.data.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(
    application: Application,
    private val registerRepository: RegisterRepository
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

    fun registerUser(name: String, email: String, password: String) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                registerRepository.registerUser(name, email, password)
                _loading.postValue(false)
                _showSuccessDialog.postValue(getApplication<Application>().getString(R.string.register_succes))
            } catch (e: HttpException) {
                _loading.postValue(false)
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, RegisterResponse::class.java)
                val errorMessage =
                    errorBody.message
                        ?: getApplication<Application>().getString(R.string.register_error)
                _showErrorDialog.postValue(errorMessage)
            } catch (e: Exception) {
                _loading.postValue(false)
                _showErrorDialog.postValue(
                    R.string.register_error_with_error_messege.toString()
                )
            }
        }
    }
}