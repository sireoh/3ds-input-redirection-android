package com.example.client__android_app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Define a Preferences DataStore instance at the top level of your Kotlin file
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsRepositoryImpl(
    private val context: Context
) : SettingsRepository {

    private object PreferencesKeys {
        val IP_ADDRESS = stringPreferencesKey("ip_address")
    }

    override fun getIpAddress(): Flow<String> {
        return context.dataStore.data
            .map { preferences ->
                // Default to an empty string if no IP address is stored
                preferences[PreferencesKeys.IP_ADDRESS] ?: ""
            }
    }

    override suspend fun saveIpAddress(ip: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IP_ADDRESS] = ip
        }
    }
}
