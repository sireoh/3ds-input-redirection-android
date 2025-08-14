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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MainActivity : ComponentActivity() {
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var mainViewModel: MainViewModel
    private lateinit var usbManager: UsbManager
    private var udpSender: UdpSender? = null
    private var inputRedirector: InputRedirector? = null
    private val usbReceiver = UsbReceiver()

    companion object {
        const val ACTION_USB_PERMISSION = "com.example.client__android_app.USB_PERMISSION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize USB Manager
        usbManager = getSystemService(Context.USB_SERVICE) as UsbManager

        // Register USB Receiver
        val filter = IntentFilter(ACTION_USB_PERMISSION)
        registerReceiver(
            usbReceiver,
            filter,
            Context.RECEIVER_NOT_EXPORTED
        )

        // Initialize Room Database and DAO
        val database = AppDatabase.getDatabase(applicationContext)

        // Initialize dependencies
        settingsRepository = SettingsRepositoryImpl(database.settingsDao())
        mainViewModel = MainViewModel(settingsRepository)

        // Check for connected USB devices
        checkUsbDevices()

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
    private fun checkUsbDevices() {
        usbManager.deviceList.values.forEach { device ->
            if (isGameController(device)) {
                Log.d("sireoh", "Controller found: ${device.deviceName}")
                requestUsbPermission(device)
            }
        }
    }

    private fun isGameController(device: UsbDevice): Boolean {
        // Common game controller vendor IDs
        return (device.vendorId == 0x045E ||  // Microsoft
                device.vendorId == 0x054C ||  // Sony
                device.vendorId == 0x046D)    // Logitech
    }

    private fun requestUsbPermission(device: UsbDevice) {
        val permissionIntent = PendingIntent.getBroadcast(
            this, 0,
            Intent(ACTION_USB_PERMISSION),
            PendingIntent.FLAG_IMMUTABLE
        )
        usbManager.requestPermission(device, permissionIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(usbReceiver)
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

    inner class UsbReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (ACTION_USB_PERMISSION == intent.action) {
                val device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE, UsbDevice::class.java)
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    Log.d("sireoh", "Permission granted for ${device?.deviceName}")
                    // Controller is now ready to use
                } else {
                    Log.d("sireoh", "Permission denied for ${device?.deviceName}")
                }
            }
        }
    }
}