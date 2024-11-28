package com.example.storie.ui.main.add_story

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storie.data.repositories.AddStoryRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(
    private val repository: AddStoryRepository
) : ViewModel() {

    private val _selectImageUri = MutableLiveData<Uri?>()
    val selectImageUri: LiveData<Uri?> = _selectImageUri

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _snackBar = MutableLiveData<String?>()
    val snackBar: MutableLiveData<String?>
        get() = _snackBar

    private val _uploadSuccess = MutableLiveData<Boolean>()
    val uploadSuccess: LiveData<Boolean>
        get() = _uploadSuccess


    private val _location = MutableLiveData<Pair<Double?, Double?>>()
    val location: LiveData<Pair<Double?, Double?>>
        get() = _location

    fun setSelectImageUri(uri: Uri?) {
        _selectImageUri.value = uri
    }

    fun setLocation(latitude: Double?, longitude: Double?) {
        _location.value = Pair(latitude, longitude)
    }

    fun uploadImage(
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.addStory(photo, description, lat, lon)
                _snackBar.value = response.message
                _uploadSuccess.value = true
            } catch (e: Exception) {
                _snackBar.value = e.message
                _uploadSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}