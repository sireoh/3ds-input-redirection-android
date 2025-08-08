package com.example.client__android_app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings_table")
data class SettingItem(
    @PrimaryKey val key: String, // e.g., "ip_address"
    val value: String
)