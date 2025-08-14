// MainActivity.kt

package com.example.client__android_app

import android.os.Bundle
import android.util.Log
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
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
                    if (udpSender == null) {
                        udpSender = UdpSender(ip)
                        inputRedirector = InputRedirector(udpSender!!)
                    } else {
                        udpSender?.updateIp(ip)
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

    // ---- Gamepad buttons ----
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        event ?: return super.onKeyDown(keyCode, event)
        if ((event.source and InputDevice.SOURCE_GAMEPAD) != 0 ||
            (event.source and InputDevice.SOURCE_JOYSTICK) != 0) {
            if (inputRedirector?.onKeyDown(keyCode) == true) return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        event ?: return super.onKeyUp(keyCode, event)
        if ((event.source and InputDevice.SOURCE_GAMEPAD) != 0 ||
            (event.source and InputDevice.SOURCE_JOYSTICK) != 0) {
            if (inputRedirector?.onKeyUp(keyCode) == true) return true
        }
        return super.onKeyUp(keyCode, event)
    }

    // ---- Joystick axes ----
    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        event ?: return super.onGenericMotionEvent(event)

        if ((event.source and InputDevice.SOURCE_JOYSTICK) != 0 &&
            event.action == MotionEvent.ACTION_MOVE) {

            val lx = event.getAxisValue(MotionEvent.AXIS_X)
            val ly = event.getAxisValue(MotionEvent.AXIS_Y)
            val rx = event.getAxisValue(MotionEvent.AXIS_Z)
            val ry = event.getAxisValue(MotionEvent.AXIS_RZ)

            inputRedirector?.onMotion(lx, ly, rx, ry)
            return true
        }

        return super.onGenericMotionEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        inputRedirector?.stopSendingFrames()
        udpSender?.close()
    }
}