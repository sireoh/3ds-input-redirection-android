package com.example.client__android_app.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.client__android_app.data.repository.SettingsRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {
    private val _ipAddress = MutableStateFlow("")
    val ipAddress: StateFlow<String> = _ipAddress

    init {
        viewModelScope.launch {
            settingsRepository.getIpAddress().collect { ip ->
                _ipAddress.value = ip
            }
        }
    }

    fun onIpAddressChanged(newIp: String) {
        _ipAddress.value = newIp
        viewModelScope.launch {
            settingsRepository.saveIpAddress(newIp)
            Log.d("sireoh", "IP Address changed to: $newIp")
        }
    }
}