package com.example.client__android_app.data.repository

import android.util.Log
import com.example.client__android_app.data.db.SettingsDao
import com.example.client__android_app.data.model.SettingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val settingsDao: SettingsDao // Inject the DAO
) : SettingsRepository {

    private companion object {
        const val KEY_IP_ADDRESS = "ip_address"
    }

    override fun getIpAddress(): Flow<String> {
        Log.d("sireoh", "Room: Getting IP Address Flow")
        return settingsDao.getSettingValue(KEY_IP_ADDRESS).map { ipValue ->
            val ip = ipValue ?: "" // Default to empty string if not found
            Log.d("sireoh", "Room: Mapping IP: $ip")
            ip
        }
    }

    override suspend fun saveIpAddress(ip: String) {
        Log.d("sireoh", "Room: saveIpAddress called with IP: $ip")
        try {
            val settingItem = SettingItem(key = KEY_IP_ADDRESS, value = ip)
            settingsDao.insertSetting(settingItem)
            Log.d("sireoh", "Room: successful save for IP: $ip")
        } catch (e: Exception) {
            Log.e("sireoh", "Room: Error saving IP", e)
        }
    }
}
