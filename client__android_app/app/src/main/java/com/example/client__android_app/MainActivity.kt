package com.example.client__android_app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.client__android_app.ui.navigation.AppNavigation
import com.example.client__android_app.ui.theme.ClientTheme
import androidx.activity.ComponentActivity
import com.example.client__android_app.data.db.AppDatabase
import com.example.client__android_app.data.repository.SettingsRepository
import com.example.client__android_app.data.repository.SettingsRepositoryImpl
import com.example.client__android_app.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room Database and DAO
        val database = AppDatabase.getDatabase(applicationContext)
        val settingsDao = database.settingsDao()

        // Initialize dependencies
        settingsRepository = SettingsRepositoryImpl(settingsDao)
        mainViewModel = MainViewModel(settingsRepository)

        enableEdgeToEdge()
        setContent {
            ClientTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Pass the ViewModel instance directly to AppNavigation
                    AppNavigation(mainViewModel = mainViewModel)
                }
            }
        }
    }
}