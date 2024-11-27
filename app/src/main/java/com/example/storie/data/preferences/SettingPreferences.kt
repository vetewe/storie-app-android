@file:Suppress("PrivatePropertyName")

package com.example.storie.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dateStore: DataStore<Preferences> by preferencesDataStore("Setting")

class SettingPreference private constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val THEME_KEY = booleanPreferencesKey("theme_setting")

    fun getThemeSetting(): Flow<Boolean> {
        return dataStore.data.map {
            it[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit {
            it[THEME_KEY] = isDarkModeActive
        }
    }

    companion object {
        @Volatile
        private var instance: SettingPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreference {
            return instance ?: synchronized(this) {
                val instance = SettingPreference(dataStore)
                this.instance = instance
                instance
            }
        }
    }
}