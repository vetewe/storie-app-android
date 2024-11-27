package com.example.storie.ui.activity.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storie.data.preferences.SettingPreference

class SettingViewModelFactory(
    private val pref: SettingPreference
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(pref) as T
        }
        throw IllegalArgumentException("unknown viewmodel class" + modelClass.name)
    }
}