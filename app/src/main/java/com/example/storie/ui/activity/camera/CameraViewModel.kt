package com.example.storie.ui.activity.camera

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CameraViewModel : ViewModel() {
    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: MutableLiveData<Uri?>
        get() = _imageUri

    fun setImageUri(uri: Uri?) {
        _imageUri.value = uri
    }
}