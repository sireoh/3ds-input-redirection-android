// MainActivity.kt

package com.example.client__android_app

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import android.view.InputDevice
import android.view.KeyEvent
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.client__android_app.ui.navigation.AppNavigation
import com.example.client__android_app.ui.theme.ClientTheme
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.client__android_app.data.db.AppDatabase
import com.example.client__android_app.data.network.InputRedirector
import com.example.client__android_app.data.network.UdpSender
import com.example.client__android_app.data.repository.SettingsRepository
import com.example.client__android_app.data.repository.SettingsRepositoryImpl
import com.example.client__android_app.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var mainViewModel: MainViewModel
    private var udpSender: UdpSender? = null
    private var inputRedirector: InputRedirector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room Database and DAO
        val database = AppDatabase.getDatabase(applicationContext)

        // Initialize dependencies
        settingsRepository = SettingsRepositoryImpl(database.settingsDao())
        mainViewModel = MainViewModel(settingsRepository)

        // Initialize UDP components
        lifecycleScope.launch {
            settingsRepository.getIpAddress().collect { ip ->
                Log.d("sireoh", "IP Address changed to: '$ip'")
                if (ip.isNotBlank()) {
                    udpSender?.updateIp(ip) ?: run {
                        Log.d("sireoh", "Creating new UdpSender with IP: $ip")
                        udpSender = UdpSender(ip)
                        inputRedirector = InputRedirector(udpSender!!)
                        Log.d("sireoh", "InputRedirector created successfully")
                    }
                } else {
                    Log.w("sireoh", "IP address is blank!")
                }
            }
        }

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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.source?.and(InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) {
            if (inputRedirector?.onKeyDown(keyCode) == true) return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.source?.and(InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) {
            if (inputRedirector?.onKeyUp(keyCode) == true) return true
        }
        return super.onKeyUp(keyCode, event)
    }
}