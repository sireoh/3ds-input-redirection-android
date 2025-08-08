package com.example.client__android_app.data.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getIpAddress(): Flow<String>
    suspend fun saveIpAddress(ip: String)
}