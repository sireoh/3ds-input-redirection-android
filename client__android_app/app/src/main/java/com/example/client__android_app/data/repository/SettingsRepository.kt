package com.example.client__android_app.data.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getIpAddress(): Flow<String> // Will now get default if not found
    suspend fun saveIpAddress(ip: String)
}