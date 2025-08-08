package com.example.client__android_app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.client__android_app.data.model.SettingItem

@Database(entities = [SettingItem::class], version = 1, exportSchema = false) // Set exportSchema to true for production apps
abstract class AppDatabase : RoomDatabase() {

    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database" // Name of your database file
                )
                    // .addMigrations(...) // Add migrations if you change schema later
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}