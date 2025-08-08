package com.example.client__android_app.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.client__android_app.data.model.SettingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSetting(setting: SettingItem)

    @Query("SELECT * FROM settings_table WHERE `key` = :key LIMIT 1")
    fun getSetting(key: String): Flow<SettingItem?> // Nullable if setting might not exist

    @Query("SELECT value FROM settings_table WHERE `key` = :key LIMIT 1")
    fun getSettingValue(key: String): Flow<String?> // Get only the value, nullable

    // Optional: if you want to delete a setting
    @Query("DELETE FROM settings_table WHERE `key` = :key")
    suspend fun deleteSetting(key: String)

    // Optional: if you want to clear all settings
    @Query("DELETE FROM settings_table")
    suspend fun clearAllSettings()
}