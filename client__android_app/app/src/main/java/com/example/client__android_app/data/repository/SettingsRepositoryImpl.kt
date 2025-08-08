package com.example.client__android_app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Define a Preferences DataStore instance at the top level of your Kotlin file
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton // Good practice for repositories if they don't hold mutable state tied to a specific scope
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
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
