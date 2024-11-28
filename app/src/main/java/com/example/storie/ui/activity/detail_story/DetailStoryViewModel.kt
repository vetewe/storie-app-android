package com.example.storie.ui.activity.detail_story

import android.app.Application
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.storie.R
import com.example.storie.data.repositories.DetailStoryRepository
import com.example.storie.data.response.Story
import com.example.storie.data.Result
import kotlinx.coroutines.launch
import java.util.Locale

class DetailStoryViewModel(
    application: Application,
    private val storyDetailStoryRepository: DetailStoryRepository
) : AndroidViewModel(application) {

    private val _storyDetail = MutableLiveData<Result<Story?>>()
    val storyDetail: LiveData<Result<Story?>>
        get() = _storyDetail

    private val _locationName = MutableLiveData<String>()
    val locationName: LiveData<String>
        get() = _locationName

    fun findDetailStory(storyId: String) {
        viewModelScope.launch {
            try {
                val result = storyDetailStoryRepository.getDetailStory(storyId)
                _storyDetail.value = result
                if (result is Result.Success) {
                    val story = result.data
                    if (story?.lat != null && story.lon != null) {
                        getLocationName(
                            story.lat.toString().toDouble(),
                            story.lon.toString().toDouble()
                        )
                    }
                }
            } catch (e: Exception) {
                _storyDetail.value = Result.Error(e.message.toString())
                Log.e(TAG, "Error: ${e.message.toString()}")
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getLocationName(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val geocoder = Geocoder(getApplication(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                if (addresses != null) {
                    if (addresses.isNotEmpty()) {
                        val location = addresses[0]
                        val province = location.adminArea ?: getApplication<Application>().getString(R.string.no_location_story)
                        _locationName.value = province
                    } else {
                        _locationName.value = getApplication<Application>().getString(R.string.no_location_story)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error: ${e.message.toString()}")
            }
        }
    }

    companion object {
        private const val TAG = "DetailStoryViewModel"
    }
}