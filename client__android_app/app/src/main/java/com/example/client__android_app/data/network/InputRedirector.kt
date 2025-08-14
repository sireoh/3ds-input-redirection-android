// InputRedirector.kt

package com.example.client__android_app.data.network

import android.view.KeyEvent
import kotlinx.coroutines.*
import kotlin.math.abs

class InputRedirector(private val udpSender: UdpSender) {

    private var hidPad = 0xFFF
    private var circleX = 0
    private var circleY = 0

    private val DEADZONE = 0.2f

    // Keep previous state to detect changes
    private var lastHidPad = hidPad
    private var lastCircleX = circleX
    private var lastCircleY = circleY

    private var job: Job? = null
    private val frameRateMs = 16L // 60Hz polling

    init {
        startSendingFrames()
    }

    private fun startSendingFrames() {
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                if (hidPad != lastHidPad || circleX != lastCircleX || circleY != lastCircleY) {
                    sendFrame()
                    lastHidPad = hidPad
                    lastCircleX = circleX
                    lastCircleY = circleY
                }
                delay(frameRateMs)
            }
        }
    }

    fun stopSendingFrames() {
        job?.cancel()
        job = null
    }

    fun onKeyDown(keyCode: Int): Boolean {
        BUTTON_MAP[keyCode]?.let { bit ->
            hidPad = hidPad and (1 shl bit).inv()
            return true
        }
        return false
    }

    fun onKeyUp(keyCode: Int): Boolean {
        BUTTON_MAP[keyCode]?.let { bit ->
            hidPad = hidPad or (1 shl bit)
            return true
        }
        return false
    }

    fun onMotion(lx: Float, ly: Float, rx: Float = 0f, ry: Float = 0f) {
        fun applyDeadzone(v: Float) = if (abs(v) < DEADZONE) 0f else v

        circleX = (applyDeadzone(lx) * 32767f).toInt()
        circleY = (-applyDeadzone(ly) * 32767f).toInt()
    }

    private fun sendFrame() {
        udpSender.sendFrame(hidPad, circleX, circleY)
    }

    companion object {
        val BUTTON_MAP = mapOf(
            KeyEvent.KEYCODE_BUTTON_A to 0,
            KeyEvent.KEYCODE_BUTTON_B to 1,
            KeyEvent.KEYCODE_BUTTON_SELECT to 2,
            KeyEvent.KEYCODE_BUTTON_START to 3,
            KeyEvent.KEYCODE_DPAD_RIGHT to 4,
            KeyEvent.KEYCODE_DPAD_LEFT to 5,
            KeyEvent.KEYCODE_DPAD_UP to 6,
            KeyEvent.KEYCODE_DPAD_DOWN to 7,
            KeyEvent.KEYCODE_BUTTON_R1 to 8,
            KeyEvent.KEYCODE_BUTTON_L1 to 9,
            KeyEvent.KEYCODE_BUTTON_X to 10,
            KeyEvent.KEYCODE_BUTTON_Y to 11
        )
    }
}